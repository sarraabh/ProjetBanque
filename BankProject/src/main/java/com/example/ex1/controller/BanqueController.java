package com.example.ex1.controller;

import com.example.ex1.dao.*;
import com.example.ex1.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class BanqueController {
    Session session = null;
    Transaction transaction = null;
    @Autowired
    private ClientRepository clr;
    @Autowired
    private EmployeeRepository er;
    @Autowired
    private CompteRepository cr;
    @Autowired
    private GroupeRepository gr;
    @Autowired
    private OperationRepository or;

    @GetMapping("/")
    public String showIndex(Model model){
        model.addAttribute("employees", er.findAll().size());
        model.addAttribute("accounts", cr.findAll().size());
        model.addAttribute("clients", clr.findAll().size());
        model.addAttribute("operations", or.findAll().size());
        String pattern = "dd MMMMM yyyy";
        String daypat = "EEEEE";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("fr", "FR"));
        String date = simpleDateFormat.format(new Date());
        SimpleDateFormat simpleDateFormat1 =new SimpleDateFormat(daypat, new Locale("fr", "FR"));
        String jour = simpleDateFormat1.format(new Date());
        model.addAttribute("date", date);
        model.addAttribute("jour", jour);
        return "index";
    }

    @GetMapping("/addaccount/{id}")
    public String showAddAccount(@PathVariable("id") long id,Model model){
        model.addAttribute("client", clr.getById(id));
        return "addaccount";
    }

    @GetMapping("/employee")
    public String showEmpolyee(Model model){
        model.addAttribute("employees", er.findAll());
        return "employeeview";
    }
    @GetMapping("/client")
    public String showClient(Model model){
        model.addAttribute("clients", clr.findAll());
        return "clientview";
    }
    @GetMapping("/accountview/{id}")
    public String showAccount(@PathVariable("id") long id,Model model){
        List<CompteCC> comptesCC = cr.findByTypeCC(id);
        List<CompteCE> comptesCE = cr.findByTypeCE(id);
        model.addAttribute("accountsCC", comptesCC);
        model.addAttribute("accountsCE", comptesCE);
        model.addAttribute("client", clr.getById(id));
        return "accountview";
    }
    @GetMapping("/group")
    public String showGroup(Model model){
        model.addAttribute("groups", gr.findAll());
        return "groupview";
    }
    @GetMapping("/group/{id}")
    public String showGroupDetail(@PathVariable("id") long id,Model model){
        model.addAttribute("group", gr.getById(id));
        model.addAttribute("employees", gr.findByGroupe(id));
        return "groupdetail";
    }
    @GetMapping("/addempgroup/{id}")
    public String addEmpGroup(@PathVariable("id") long id,Model model){
        model.addAttribute("group", gr.getById(id));
        model.addAttribute("employees", er.findAll());
        return("addEmpGroup");
    }
    @RequestMapping(value = "/saveemptogroup/{id}",method = RequestMethod.POST)
    public String saveEmpGroup(@PathVariable("id") long id,Model model, @ModelAttribute("employee") Employee employee){

            Groupe g = gr.getById(id);
            List<Groupe> lg = employee.getGroupes();
            List<Employee> le = g.getEmployes();
            if (!(le.contains(employee))){
            lg.add(g);
            le.add(employee);
            employee.setGroupes(lg);
            g.setEmployes(le);
            er.save(employee);
            gr.save(g);
            }else {

            }
    return "redirect:/group/{id}";
    }
    @GetMapping(value = "/deleteempgroup/{idg}/{ide}")
    public String DeleteEmpGroup(@PathVariable("idg") long idg,Model model, @PathVariable("ide") long ide){
        Groupe g = gr.getById(idg);
        Employee employee = er.getById(ide);
        List<Employee> le = g.getEmployes();
        List<Groupe> lg = employee.getGroupes();
        le.remove(employee);
        lg.remove(g);
        employee.setGroupes(lg);
        g.setEmployes(le);
        gr.save(g);
        er.save(employee);
        return "redirect:/group/{idg}";
    }
    @GetMapping("/addgroup")
    public String ShowAddGroup(Model model, Groupe groupe){
        model.addAttribute("groupe", groupe);
        return"addgroup";
    }
    @RequestMapping(value = "/savegroup",method = RequestMethod.POST)
    public ModelAndView AddGroup(@ModelAttribute("groupe") Groupe groupe){
        gr.save(groupe);
        return new ModelAndView("redirect:/group");
    }
    @RequestMapping(value= "/updategroup/{code}", method = RequestMethod.POST)
    public ModelAndView updateGroup(@PathVariable("code") long code, @ModelAttribute("groupe") Groupe groupe,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return new ModelAndView("redirect:/addgroup");
        }
        Groupe g = gr.findById(code).get();
        g.setNomGroupe(groupe.getNomGroupe());
        gr.save(g);
        return new ModelAndView("redirect:/group");
    }
    @GetMapping("/deletegroup/{id}")
    public String deleteGroup(@PathVariable("id") long id, Model model) {
        Groupe g = gr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        gr.delete(g);
        return "redirect:/group";
    }
    @GetMapping("/operation/{id}")
    public String showOperation(@PathVariable("id") long id,Model model){
        model.addAttribute("compte", cr.getById(id));
        model.addAttribute("operationsR", or.findAllByTypeR(id));
        model.addAttribute("operationsV", or.findAllByTypeV(id));
        return "operationview";
    }
    @GetMapping("/addoperation/{id}")
    public String showAddOperation(@PathVariable("id") long id,Model model, Operations operations){
        model.addAttribute("compte", cr.getById(id));
        model.addAttribute("operation", operations);
        model.addAttribute("employees", er.findAll());
        return "addoperation";
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @RequestMapping(value = "/saveoperation/{id}", method = RequestMethod.POST, params="action=Retrait")
    public ModelAndView AddRetrait(@PathVariable("id") long id, @ModelAttribute("operation") Retrait operations){
    Compte c = cr.getById(id);
       Date todaysdate = new Date();
       operations.setDateOperation(todaysdate);
    if (c.getSolde()>=operations.getMontant()){
        c.setSolde(c.getSolde()-operations.getMontant());
        operations.setCompte(c);
        cr.save(c);
        or.save(operations);
        return new ModelAndView("redirect:/operation/{id}");
    }else {
        return new ModelAndView("redirect:/addoperation/{id}");
    }
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @RequestMapping(value = "/saveoperation/{id}", method = RequestMethod.POST, params="action=Versement")
    public ModelAndView AddVersement(@PathVariable("id") long id, @ModelAttribute("operation") Versement operations){
        Compte c = cr.getById(id);
        Date todaysdate = new Date();
        operations.setDateOperation(todaysdate);
        operations.setCompte(c);
        c.setSolde(c.getSolde()+operations.getMontant());
        or.save(operations);
        cr.save(c);
        return new ModelAndView("redirect:/operation/{id}");
    }
    @GetMapping("/deleteoperation/{id}")
    public String deleteOperation(@PathVariable("id") long id, Model model) {
        Operations o = or.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        or.delete(o);
        return "redirect:/";
    }
    @GetMapping("/addemployee")
    public String ShowAddEmp(Model model, Employee employee){
        model.addAttribute("employee", employee);
        model.addAttribute("employees", er.findAll());
        return"addemployee";
    }
    @RequestMapping(value = "/saveemployee",method = RequestMethod.POST)
    public ModelAndView AddEmp(@ModelAttribute("employee") Employee employee){
        er.save(employee);
        return new ModelAndView("redirect:/employee");
    }
    @RequestMapping(value= "/updateemp/{code_emp}", method = RequestMethod.POST)
    public ModelAndView updateUser(@PathVariable("code_emp") long code_emp, @ModelAttribute("e") Employee e,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return new ModelAndView("redirect:/addemployee");
        }
        Employee em = er.findById(code_emp).get();
        em.setSolde(e.getSolde());
        em.setEmployee_sup(e.getEmployee_sup());
        er.save(em);
        return new ModelAndView("redirect:/employee");
    }
    @GetMapping("/deleteemp/{id}")
    public String deleteEt(@PathVariable("id") long id, Model model) {
        Employee e= er.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        er.delete(e);
        return "redirect:/employee";
    }
    @GetMapping("/addclient")
    public String ShowAddClt(Model model, Client clt){
        model.addAttribute("client", clt);
        return"addclient";
    }
    @RequestMapping(value = "/saveclient",method = RequestMethod.POST)
    public ModelAndView AddCLI(@ModelAttribute("client") Client client){
        clr.save(client);
        return new ModelAndView("redirect:/client");
    }
    @RequestMapping(value= "/updatecli/{codecli}", method = RequestMethod.POST)
    public ModelAndView updateClient(@PathVariable("codecli") long codecli, @ModelAttribute("c") Client c,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return new ModelAndView("redirect:/addclient");
        }
        Client cli = clr.findById(codecli).get();
        cli.setNomcli(c.getNomcli());
        clr.save(cli);
        return new ModelAndView("redirect:/client");
    }
    @GetMapping("/deletecli/{id}")
    public String deleteCLI(@PathVariable("id") long id, Model model) {
        Client c= clr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        clr.delete(c);
        return "redirect:/client";
    }
    @GetMapping("/addaccountCC/{id}")
    public String ShowAddAccCC(@PathVariable("id") long id,Model model, CompteCC compte){
        model.addAttribute("compte", compte);
        model.addAttribute("employees", er.findAll());
        model.addAttribute("client", clr.getById(id));
        return "addaccountCC";
    }
    @GetMapping("/addaccountCE/{id}")
    public String ShowAddAccCE(@PathVariable("id") long id,Model model, CompteCE compte){
        model.addAttribute("compte", compte);
        model.addAttribute("employees", er.findAll());
        model.addAttribute("client", clr.getById(id));
        return "addaccountCE";
    }
    @RequestMapping(value = "/saveaccountCC/{id}",method = RequestMethod.POST)
    public ModelAndView AddAccCC(@PathVariable("id") long id,@ModelAttribute("compte") CompteCC compte){
        compte.setClient(clr.getById(id));
        compte.setDatecreation(new Date());
        cr.save(compte);
        return new ModelAndView("redirect:/accountview/{id}");
    }
    @RequestMapping(value = "/saveaccountCE/{id}",method = RequestMethod.POST)
    public ModelAndView AddAccCE(@PathVariable("id") long id,@ModelAttribute("compte") CompteCE compte){
        compte.setClient(clr.getById(id));
        compte.setDatecreation(new Date());
        cr.save(compte);
        return new ModelAndView("redirect:/accountview/{id}");
    }
    @RequestMapping(value= "/updateaccountCC/{code}", method = RequestMethod.POST)
    public ModelAndView updateAccCC(@PathVariable("code") long code, @ModelAttribute("acc") CompteCC acc,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return new ModelAndView("redirect:/client");
        }
        CompteCC compte = (CompteCC) cr.findById(code).get();
        compte.setDecouvert(acc.getDecouvert());

        cr.save(compte);
        return new ModelAndView("redirect:/client");
    }
    @RequestMapping(value= "/updateaccountCE/{code}", method = RequestMethod.POST)
    public ModelAndView updateAccCE(@PathVariable("code") long code, @ModelAttribute("acc") CompteCE acc,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return new ModelAndView("redirect:/client");
        }
        CompteCE compte = (CompteCE) cr.findById(code).get();
        compte.setTaux(acc.getTaux());
        cr.save(compte);
        return new ModelAndView("redirect:/client");
    }
    @GetMapping("/deletecompte/{id}")
    public String deleteCompte(@PathVariable("id") long id, Model model) {
        Compte c= cr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        cr.delete(c);
        return "redirect:/client";
    }
}
