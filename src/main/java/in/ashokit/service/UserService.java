package in.ashokit.service;

import in.ashokit.bind.LoginForm;
import in.ashokit.bind.SignUpForm;
import in.ashokit.bind.UnlockForm;

public interface UserService {
  
	public boolean signUp(SignUpForm form);
	
	public  boolean unlockAccount(UnlockForm form);
	
	public String login(LoginForm form);
	
	public boolean forgotPwd(String email);
}
