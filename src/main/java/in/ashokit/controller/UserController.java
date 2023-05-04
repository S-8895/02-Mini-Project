package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.bind.LoginForm;
import in.ashokit.bind.SignUpForm;
import in.ashokit.bind.UnlockForm;
import in.ashokit.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//submit the form
	@PostMapping("/signup")
	public String handleSignUp(@ModelAttribute("user")SignUpForm form,Model model) {
		
		boolean status = userService.signUp(form);
		
		if(status) {
			model.addAttribute("succmsg", "Account created,Check your Email");
		}else {
		 
			model.addAttribute("errmsg", "choose unique email");
		}
		
		return "signup";
	}
	
	
	
	//load the empty signup page
	@GetMapping("/signup")
	   public String signUpPage(Model model) {
		
		model.addAttribute("user", new SignUpForm());
		   return "signup"; 
	   }
	
	//unlock page
	

	@GetMapping("/unlock")       //taking data from query param.
	   public String unlockPage(@RequestParam String email, Model model) {
		
		//setting the data binding obj
		UnlockForm unlockformObj = new UnlockForm();
		unlockformObj.setEmail(email);
		//sending binding obj to UI
		model.addAttribute("unlock", unlockformObj);
		//return page
		   return "unlock"; 
	   }
	
	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm unlock , Model model) {
		
		System.out.println(unlock);
		
		if(unlock.getNewPwd().equals(unlock.getConfirmPwd())) {
			
			boolean status = userService.unlockAccount(unlock);
			
			if(status) {
				
				model.addAttribute("succMsg","Your Account unlock successfully...");

				
			}else {
				
				model.addAttribute("errMsg","Given Temporary password is incorrect....,Check your email");

			}
			
			
			
		}else {
			model.addAttribute("errMsg","New pwd and confirm pwd should be same");
		}
		
		
		return "unlock";
	}
	
	
	
	
	
	
	@GetMapping("/login")
   public String logInPage(Model model) {
	   
		model.addAttribute("loginForm", new LoginForm());
		
		return "login"; 
   }
	
	
	@PostMapping("/login")
	   public String logIn(@ModelAttribute("loginForm") LoginForm loginform , Model model) {
		   
		String status = userService.login(loginform);
		
		if(status.contains("Success")){
			
			return "redirect:/dashboard";
		}
		
		model.addAttribute("errMsg", status);
			return "login"; 
	   }
	
	
	
	@GetMapping("/forgot")
	   public String forgotPwPage() {
		   return "forgotpwd"; 
	   }
	
	

	@PostMapping("/forgotPwd")
	   public String forgotPwd(@RequestParam("email") String email,Model model) {
		   
		System.out.println(email);
		
		boolean status = userService.forgotPwd(email);
		
		if(status) {
			//send success msg
			model.addAttribute("succMsg","Pwd sent to your email");
			
		}else {
			//send error msg
			model.addAttribute("errMsg","Invalid Email");

		}
		
		
		
		return "forgotpwd"; 
	   }
	
	
	
	
	
	
	
	
}
