package in.ashokit.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.bind.LoginForm;
import in.ashokit.bind.SignUpForm;
import in.ashokit.bind.UnlockForm;
import in.ashokit.entity.UserDtlsEntity;
import in.ashokit.repo.UserDtlsRepo;
import in.ashokit.util.EmailUtils;
import in.ashokit.util.PwdUtils;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDtlsRepo repo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;
	
	
	@Override
	public String login(LoginForm form) {
		
		UserDtlsEntity entity = repo.findByEmailAndPwd(form.getEmail(), form.getPwd());
		
		if(entity == null) {
			return "Invalid Credential";
		}
	
		if(entity.getAccStatus().equals("LOCKED")) {
			
			return "Your Account Locked";
		}
		
		//create session and store user data in session
		session.setAttribute("userId", entity.getUserId());
		
		return "Success";
	}
	
	
	
	    @Override
	    public boolean unlockAccount(UnlockForm form) {
	    	
	    	UserDtlsEntity entity = repo.findByEmail(form.getEmail());
	    	
	    	if(entity.getPwd().equals(form.getTempPwd())) {
	    		
	    		
	    		entity.setPwd(form.getNewPwd());
	    		
	    		entity.setAccStatus("UNLOCKED");
	    		 
	    		repo.save(entity);
	    		 
	    		return true;
	    		
	    	}else {
 	    		return false;
	    	}
	    	
	    
	    }
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean signUp(SignUpForm form) {
		
		  UserDtlsEntity user = repo.findByEmail(form.getEmail());
		  if(user!=null) {
			  return false;
		  }
		
		
		//TODO:Copy data from Binding obj to Entity
		
		UserDtlsEntity entity = new UserDtlsEntity();
		
		BeanUtils.copyProperties(form, entity);
		
		
		//ToDo:generate random pwd and set to obj
		
		String temppwd = PwdUtils.generateRandomPwd();
		entity.setPwd(temppwd);
		
		//TODO:set the Aaccount status LOCKED
		entity.setAccStatus("LOCKED");
		
		
		//TODO:INSERT RECORD
		repo.save(entity);
		
		//TODO:Send Email to Unlock account
		String to = form.getEmail();
		String subject = "Unlock Your Account ! ASHOK IT";
		// we take string buffer bcz string is immutable ,so when we change it will create new obj.
		StringBuffer body = new StringBuffer("");
		body.append("<h1> Use below temp password to unlock your Account</h1>");
		body.append("Temp Pwd :  " + temppwd);
		
		//give the link to another line
		body.append("<br/>");
		
		body.append("<a href=\"http://localhost:9098/unlock?email="+to+"\">Click Here To Unlock Your Account</a>");
		
		
		emailUtils.sendEmail(to, subject, body.toString());
		
		
		return true;
	}
	
	
	@Override
	public boolean forgotPwd(String email) {

		//check record present in db with given email
		UserDtlsEntity entity = repo.findByEmail(email);
		//if record is not available send false
		  
		if(entity==null) {
			
			return false;
		}
		
		//if record available send Pwd to email and return true
		
		String subject = "Recover Password";
		
		String body ="Your Password ::"+entity.getPwd();
		
		
		emailUtils.sendEmail(email, subject, body);
		
		
		
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
