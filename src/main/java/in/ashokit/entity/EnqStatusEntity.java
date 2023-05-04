package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="enq_status_tbl")
public class EnqStatusEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enqStatusId;
	private String enqStatusName;
}
