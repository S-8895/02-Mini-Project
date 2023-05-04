package in.ashokit.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name="std_enq_tbl")
@Data
public class StudentEnqEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stdEnqId;
	
	private String stdName;
	
	private Long mob;
	
	private String classMode;
	
	private String courseName;
	
	private String enqStatus;
	
	@CreationTimestamp
	private LocalDate dateCreated;
	
	@UpdateTimestamp
	private LocalDate lastUpdate;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private UserDtlsEntity user;
}
