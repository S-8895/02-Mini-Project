package in.ashokit.service;

import java.util.List;

import in.ashokit.bind.DashbordResponse;
import in.ashokit.bind.EnquiryForm;
import in.ashokit.bind.EnquirySearchCriteria;
import in.ashokit.entity.StudentEnqEntity;

public interface EnquiryService {
	
	public List<String> getCourseName();
	
	public List<String> getEnqStatus();

	public DashbordResponse getDashbordData(Integer userId);
	
	public boolean saveEnquiry(EnquiryForm form);
	 
   public List<StudentEnqEntity> getEnquiries();
	
   public List<StudentEnqEntity> getFilteredEnq(EnquirySearchCriteria criteria,Integer userId);

}

