package in.ashokit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.bind.DashbordResponse;
import in.ashokit.bind.EnquiryForm;
import in.ashokit.bind.EnquirySearchCriteria;
import in.ashokit.entity.CourseEntity;
import in.ashokit.entity.EnqStatusEntity;
import in.ashokit.entity.StudentEnqEntity;
import in.ashokit.entity.UserDtlsEntity;
import in.ashokit.repo.CourseRepo;
import in.ashokit.repo.EnqStatusRepo;
import in.ashokit.repo.StudentEnqRepo;
import in.ashokit.repo.UserDtlsRepo;
@Service
public class EnquiryServiceImpl implements EnquiryService {
	@Autowired
	private UserDtlsRepo userDtlsRepo;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Autowired
	private StudentEnqRepo stdEnqRepo;
	
	@Autowired
	private HttpSession session;
	

	@Override
	public List<String> getCourseName() {
		
		List<CourseEntity> findAll = courseRepo.findAll();
		
		List<String> names = new ArrayList();
		
		for(CourseEntity entity : findAll) {
			names.add(entity.getCourseName());
		}
				
				
		return names;
	}

	@Override
	public List<String> getEnqStatus() {
            
		List<EnqStatusEntity> findAll = enqStatusRepo.findAll();
		
		List<String> status = new ArrayList();
		
	   for(EnqStatusEntity entity :findAll ) {
		   status.add(entity.getEnqStatusName());
	   }
		
		
		return status;
	}

	@Override
	public DashbordResponse getDashbordData(Integer userId) {
		
		DashbordResponse response = new DashbordResponse();
	
		Optional<UserDtlsEntity> findById= userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			UserDtlsEntity userEntity = findById.get();
		
		
			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();
			
			Integer totalCnt = enquiries.size();
			
			Integer enrolledCnt = enquiries.stream()
					.filter(e->e.getEnqStatus().equals("Enrolled"))
					    .collect(Collectors.toList()).size();
			
		   
		  
			Integer lostCnt  = enquiries.stream()
					.filter(e->e.getEnqStatus().equals("Lost"))
					    .collect(Collectors.toList()).size();
			
			
			response.setTotalEnquiriesCnt(totalCnt);
			response.setEnrolledCnt(enrolledCnt);
			response.setLostCnt(lostCnt);
		
		}
		
		return response;
	}

	@Override
	public boolean saveEnquiry(EnquiryForm form) {
		
		StudentEnqEntity stdEnqEntity = new StudentEnqEntity();
		
		BeanUtils.copyProperties(form, stdEnqEntity);
		
		Integer userId =(Integer)session.getAttribute("userId");
		
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findById(userId).get();
		stdEnqEntity.setUser(userDtlsEntity);
		
		stdEnqRepo.save(stdEnqEntity);
		
		return true;
	}

	
	
	@Override
	public List<StudentEnqEntity> getEnquiries() {
		
		Integer userId = (Integer)session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			
			UserDtlsEntity userDtlsEntity = findById.get();
			
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			
			return enquiries;
		}
		
		
		
		return null;
	}
	
	
	
	@Override
	public List<StudentEnqEntity> getFilteredEnq(EnquirySearchCriteria criteria, Integer userId) {
		
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			
			UserDtlsEntity userDtlsEntity = findById.get();
			
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			
			//filter logic
			
			if(criteria!=null & !"".equals(criteria.getCourseName())) {
				enquiries	= enquiries.stream().filter(e->e.getCourseName().equals(criteria.getCourseName())).collect(Collectors.toList());
			}
			
			if(criteria!=null & !"".equals(criteria.getEnqStatus())) {
				enquiries = enquiries.stream().filter(e->e.getEnqStatus().equals(criteria.getEnqStatus())).collect(Collectors.toList());
			}
			
			if(criteria!=null & !"".equals(criteria.getClassMode())) {
				enquiries = enquiries.stream().filter(e->e.getClassMode().equals(criteria.getClassMode())).collect(Collectors.toList());
			}
			
			
			return enquiries;
		}
		
		
		
		return null;
	}
	
	
	



}
