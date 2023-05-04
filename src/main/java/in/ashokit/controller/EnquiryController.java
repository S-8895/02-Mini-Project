package in.ashokit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.bind.DashbordResponse;
import in.ashokit.bind.EnquiryForm;
import in.ashokit.bind.EnquirySearchCriteria;
import in.ashokit.entity.StudentEnqEntity;
import in.ashokit.service.EnquiryService;
@Controller
public class EnquiryController {
	
	 @Autowired
	  private EnquiryService  enquiryService;
	 
	    
	  @Autowired
	  private HttpSession session;
	
	
	   @GetMapping("/logout")
	  public String logout() {
		   session.invalidate();
		  return "index";
	  }
	
	
	
	   @GetMapping("/dashboard")
	   public String dashbordPage(Model model) {
		   
		 Integer userId = 
				   (Integer) session.getAttribute("userId");
		   
		   
		   //logic to fetch data to dashboard
		  DashbordResponse dashboardData = 
				         enquiryService.getDashbordData(userId);
		  
		  model.addAttribute("dashboardData", dashboardData);
		   
		   return "dashboard"; 
	   }
	   
	   @GetMapping("/enquiry")
	   public String addEnquiryPage(Model model) {
		   
		   initForm(model);
		   
		   return "add-enquiry"; 
		   
		  
	   
	   }
	   
	   private void initForm(Model model) {
		   //get courses for drop down
		   List<String> courseName = enquiryService.getCourseName();
		   
		   //get enq status for dropdown
		   List<String> enqStatus = enquiryService.getEnqStatus();
		   
		   //create binding class object
		   
		   EnquiryForm form = new EnquiryForm();
		   
		   //set data in model object
		   model.addAttribute("courses",courseName );
		   model.addAttribute("enqstatus", enqStatus);
		   model.addAttribute("formobj", form);
		   
		  
	   }
	   
	   
	   
	   
	   
	   @PostMapping("/addEnq")
	   public String addEnqiury(@ModelAttribute("formobj") EnquiryForm formobj,Model model) {

		   boolean status = enquiryService.saveEnquiry(formobj);
		   
		   if(status) {
			   
			   model.addAttribute("succMsg","Enquiry Added");
			   
		   }else {
			   
			   model.addAttribute("errMsg","Problem Occured");
		   }
		   
		   return "add-enquiry"; 
	   }
	   
	   
	   
	   @GetMapping("/enquires")
	   public String viewEnquiryPage(EnquirySearchCriteria criteria,Model model) {
		   
		   initForm(model);
		   
		   model.addAttribute("searchForm",new EnquirySearchCriteria());
		   
		   List<StudentEnqEntity> enquiries = enquiryService.getEnquiries();
		   
		   model.addAttribute("enquiries", enquiries);
		   
		   
		   return "view-enquiries"; 
	   }

   @GetMapping("/filter-enquiries")
   public String getFilteredEnqs(@RequestParam String cname,@RequestParam String status,@RequestParam String mode,Model model) {
	   
	   EnquirySearchCriteria criteria = new EnquirySearchCriteria();
	   criteria.setClassMode(mode);
	   criteria.setCourseName(cname);
	   criteria.setEnqStatus(status);
	  
	   Integer userId = (Integer) session.getAttribute("userId");
	   
	   List<StudentEnqEntity> filteredEnq = enquiryService.getFilteredEnq(criteria,userId);
	   
	   model.addAttribute("enquiries", filteredEnq);
	   
	   
	   return"filter-enquiries";
   }










}

