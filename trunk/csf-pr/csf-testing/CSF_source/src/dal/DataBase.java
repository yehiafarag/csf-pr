package dal;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.model.beans.User;

public class DataBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
	private Connection conn_i = null;
	
	private String url ;
	private String dbName;
	private String driver ;
	private String userName ;
	private String password ;

	//create tables
	public  DataBase(String url,String dbName,String driver,String userName, String password )
	{
		
		this.url = url;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password = password;
		try{		
			//Class.forName(driver).newInstance();
		}catch(Exception e){};
		
	}
	
	public boolean createTables()//create CSF the database tables if not exist
	{
		
		
		try{
			try{
				if(conn_i == null || conn_i.isClosed()){
				Class.forName(driver).newInstance();
				conn_i = DriverManager.getConnection(url+"mysql", userName, password);
				}
				Statement statement = conn_i.createStatement();
				String csfSQL = "CREATE DATABASE IF NOT exists  "+dbName;
				statement.executeUpdate(csfSQL);
				conn_i.close();
			}catch(Exception e){e.printStackTrace();}
			if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}try{
				  Statement st = conn.createStatement();
				  //CREATE TABLE  `users_table`
				  String users_table =  "CREATE TABLE IF NOT EXISTS  `users_table`  (`id` INT( 20 ) NOT NULL AUTO_INCREMENT ,  `password` VARCHAR( 100 ) NOT NULL , `admin` VARCHAR( 5 ) NOT NULL DEFAULT  'FALSE',  `user_name` VARCHAR( 20 ) NOT NULL , PRIMARY KEY (  `user_name` ) , INDEX (  `id` )) ENGINE = MYISAM ;";
				  st.executeUpdate(users_table);				  
				  
			  //CREATE TABLE `experiments_table`
				  String experiments_table =  "CREATE  TABLE IF NOT EXISTS`experiments_table` (  `exp_id` int(11) NOT NULL auto_increment,  `name` varchar(50) NOT NULL,  `fractions_number` int(11) NOT NULL default '0',  `ready` int(11) NOT NULL default '0',  `uploaded_by` varchar(20) NOT NULL,  `peptide_file` int(2) NOT NULL default '0',"
						  +" `species` varchar(100) NOT NULL,  `sample_type` varchar(100) NOT NULL,  `sample_processing` varchar(100) NOT NULL,  `instrument_type` varchar(100) NOT NULL,  `frag_mode` varchar(100) NOT NULL,  `proteins_number` int(11) NOT NULL default '0',  `peptides_number` int(11) NOT NULL default '0',  `email` varchar(40) NOT NULL,  `pblication_link` varchar(100) NOT NULL default 'NOT AVAILABLE'," 
						  +" PRIMARY KEY  (`exp_id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";

				  st.executeUpdate(experiments_table);
			  //CREATE TABLE proteins_table
				  String proteins_table = "CREATE TABLE IF NOT EXISTS `proteins_table` ("+
			   		"`id` int(11) NOT NULL auto_increment,  `accession` varchar(30) NOT NULL,"+
			   		"`description` varchar(500) NOT NULL default 'No Description Available',"+
			   		"PRIMARY KEY  (`accession`),  KEY `id` (`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
				  st.executeUpdate(proteins_table);
				  
				//CREATE TABLE experiment_protein_table
				  String experiment_protein_table = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (  `exp_id` int(11) NOT NULL,  `prot_accession` varchar(30) NOT NULL,"
					 + "`other_protein(s)` varchar(40) default NULL,  `protein_inference_class` varchar(100) default NULL,  `sequence_coverage(%)` double default NULL,`observable_coverage(%)` double default NULL,"
					 +"`confident_ptm_sites` varchar(50) default NULL,  `number_confident` varchar(50) default NULL,  `other_ptm_sites` varchar(50) default NULL,  `number_other` varchar(50) default NULL, `number_validated_peptides` int(11) default NULL,"
					 +"`number_validated_spectra` int(11) default NULL, `em_pai` double default NULL,  `nsaf` double default NULL,  `mw_(kDa)` double default NULL,  `score` double default NULL,  `confidence` double default NULL,  `starred` varchar(5) default NULL,"
					 +"KEY `exp_id` (`exp_id`),  KEY `prot_accession` (`prot_accession`),FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE,FOREIGN KEY (`prot_accession`) REFERENCES proteins_table (`accession`) ON DELETE CASCADE) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
				  st.executeUpdate(experiment_protein_table);
			  
				//CREATE TABLE experiment_fractions_table
				  String experiment_fractions_table = "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` (  `exp_id` int(11) NOT NULL,`fraction_id` int(11) NOT NULL auto_increment,  `min_range` double NOT NULL default '0',"
						  +"  `max_range` double NOT NULL default '0',  PRIMARY KEY  (`fraction_id`),  KEY `exp_id` (`exp_id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
				  st.executeUpdate(experiment_fractions_table);
				  
				  //  CREATE TABLE  `experiment_peptides_table`
				  String experiment_peptide_table ="CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (  `exp_id` INT NOT NULL DEFAULT  '0',  `pep_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE  ) ENGINE = MYISAM ;";
				  st.executeUpdate(experiment_peptide_table);
				  
				  // CREATE TABLE  `proteins_peptides_table`
				  String proteins_peptides_table = "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` ( `protein` VARCHAR( 70 ) NULL ,`other_protein(s)` VARCHAR( 70 ) NULL ,"
						 +" `peptide_protein(s)` VARCHAR( 500 ) NULL , `other_protein_description(s)` VARCHAR( 500 ) NULL , `peptide_proteins_description(s)` VARCHAR( 1000 ) NULL , `aa_before` VARCHAR( 200 ) NULL ,"
						 +" `sequence` VARCHAR( 100 )  NULL ,  `aa_after` VARCHAR( 200 ) NULL , `peptide_start` VARCHAR( 500 )  NULL ,  `peptide_end` VARCHAR( 500 )  NULL, `variable_modification` VARCHAR( 70 ) NULL ,"
						 +" `location_confidence` VARCHAR( 70 ) NULL , `precursor_charge(s)` VARCHAR( 70 ) NULL , `number_of_validated_spectra` INT( 20 ) NULL , `score` DOUBLE NOT NULL DEFAULT  '0.00', `confidence` DOUBLE NOT NULL DEFAULT  '0.00',"
						 +" `peptide_id` INT( 50 ) NOT NULL DEFAULT  '0' ,FOREIGN KEY (`peptide_id`) REFERENCES experiment_peptides_table (`pep_id`) ON DELETE CASCADE ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
				  st.executeUpdate(proteins_peptides_table);
				  
				//CREATE TABLE fractions_table
				  String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (  `fraction_id` int(11) NOT NULL,`prot_accession` varchar(30) NOT NULL,"
						  +"`number_peptides` int(11) NOT NULL default '0',  `number_spectra` int(11) NOT NULL default '0',`average_ precursor_intensity` double default NULL,"
						  +"KEY `prot_accession` (`prot_accession`), KEY `fraction_id` (`fraction_id`),	FOREIGN KEY (`prot_accession`) REFERENCES proteins_table(`accession`) ON DELETE CASCADE,"
						  +"FOREIGN KEY (`fraction_id`) REFERENCES experiment_fractions_table(`fraction_id`) ON DELETE CASCADE	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
				  st.executeUpdate(fractions_table);
				 
				 
				  
			  }
			  catch(SQLException s){
				  s.printStackTrace();
				  return false;
			  }
			  ////conn.close();
			}
			catch (Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		
	}
	
//		Storing Data
	
	public boolean setProteinFile(ExperimentBean exp)
	{
		if(exp.getExpId() == -1)//new Experiment
		{
			PreparedStatement insertExpStat = null;
			int id = 0;
			int test=0;
			
			
			
		    String insertExp = "INSERT INTO  `"+dbName+"`.`experiments_table` (`name`,`ready` ,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`proteins_number` ,	`email` ,`pblication_link`)VALUES (?,?,?,?,?,?,?,?,?,?,?) ;";
		    try {		    	
		    	if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}insertExpStat = conn.prepareStatement(insertExp,Statement.RETURN_GENERATED_KEYS); 
			  		    insertExpStat.setString(1, exp.getName().toUpperCase());
			  		    insertExpStat.setInt(2,1);   
			  		    insertExpStat.setString(3, exp.getUploadedByName().toUpperCase()); 
			  		    insertExpStat.setString(4, exp.getSpecies().toUpperCase()); 
			  		    insertExpStat.setString(5, exp.getSampleType().toUpperCase());
			  		    insertExpStat.setString(6, exp.getSampleProcessing().toUpperCase());
			  		    insertExpStat.setString(7, exp.getInstrumentType().toUpperCase()); 
			  		    insertExpStat.setString(8, exp.getFragMode().toUpperCase()); 
			  		    insertExpStat.setInt(9, exp.getProteinsNumber()); 			  		    
			  		    insertExpStat.setString(10, exp.getEmail().toUpperCase()); 
			  		    if(exp.getPublicationLink() != null)
			  		    		insertExpStat.setString(11, exp.getPublicationLink().toUpperCase()); 
			  		    else
			  		    	insertExpStat.setString(11, "NOT AVAILABLE"); 				  		    	
			  		    insertExpStat.executeUpdate();
				    	ResultSet rs  = insertExpStat.getGeneratedKeys();
				    	while(rs.next()){
				    		id = rs.getInt(1);
				    	}
				    	for(ProteinBean pb: exp.getProteinList().values())
				    	{
				    		test = this.insertProteinExper(driver, url, dbName, userName, password, id, pb);
				    		test = this.insertProt(pb.getAccession(),pb.getDescription());				    		
				    	}
				    	////conn.close();
			    } catch (Exception e ) {   e.printStackTrace();return false  ;} 
		    
			  if(test > 0 ) 
				  return true;
		}
		
			return false;
	}
	public boolean setProteinFractionFile(ExperimentBean exp)
	{
		if(exp.getExpId() == -1)//new Experiment
		{
			PreparedStatement insertExpStat = null;
			PreparedStatement insertFractExpStat = null;
			int expId = 0;
			int fractId = 0;
			int test=0;
		    String insertExp = "INSERT INTO  `"+dbName+"`.`experiments_table` (`name`,`ready`,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`fractions_number` ,	`email` ,`pblication_link`)VALUES (?,?,?,?,?,?,?,?,?,?,?) ;";
		    String insertFractExp = "INSERT INTO  `"+dbName+"`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`) VALUES (?,?,?) ;";
		    	try {		    	
		    		if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}insertExpStat = conn.prepareStatement(insertExp,Statement.RETURN_GENERATED_KEYS); 
			  		    insertExpStat.setString(1, exp.getName().toUpperCase());
			  		    insertExpStat.setInt(2,1);   
			  		    insertExpStat.setString(3, exp.getUploadedByName().toUpperCase()); 

			  		    insertExpStat.setString(4, exp.getSpecies().toUpperCase()); 

			  		    insertExpStat.setString(5, exp.getSampleType().toUpperCase()); 

			  		    insertExpStat.setString(6, exp.getSampleProcessing().toUpperCase()); 

			  		    insertExpStat.setString(7, exp.getInstrumentType().toUpperCase()); 

			  		    insertExpStat.setString(8, exp.getFragMode().toUpperCase()); 

			  		    insertExpStat.setInt(9, exp.getFractionsNumber()); 			  		    
			  		    insertExpStat.setString(10, exp.getEmail().toUpperCase()); 
			  		    if(exp.getPublicationLink() != null)
		  		    		insertExpStat.setString(11, exp.getPublicationLink().toUpperCase()); 
			  		    else
			  		    	insertExpStat.setString(11, "NOT AVAILABLE"); 
			  		    insertExpStat.executeUpdate();
				    	ResultSet rs  = insertExpStat.getGeneratedKeys();
				    	while(rs.next()){
				    		expId = rs.getInt(1);
				    	}
				    	for(FractionBean fb:exp.getFractionsList().values())
				    	{
					    	insertFractExpStat = conn.prepareStatement(insertFractExp,Statement.RETURN_GENERATED_KEYS);
					    	insertFractExpStat.setInt(1,expId); 
					    	insertFractExpStat.setDouble(2,fb.getMinRange()); 
					    	insertFractExpStat.setDouble(3,fb.getMaxRange()); 
					    	insertFractExpStat.executeUpdate();
					    	rs  = insertFractExpStat.getGeneratedKeys();
					    	while(rs.next()){
					    		fractId = rs.getInt(1);
					    	}				    	
					    	
					    	for(ProteinBean pb: fb.getProteinList().values())
						    {
						    	test = this.insertProteinFract(fractId, pb);
						    	test = this.insertProt(pb.getAccession(),pb.getDescription());
						    		
						    }			    		
					    	
				    	}
				    	////conn.close();			        
			    } catch (Exception e ) {   e.printStackTrace();return false  ;} 
			  if(test > 0 ) 
				  return true;
		}
	
		return false;
	}
	public boolean setPeptideFile(ExperimentBean exp) {
		if(exp.getExpId() == -1)//new Experiment
		{
			PreparedStatement insertExpStat = null;
			PreparedStatement insertPeptExpStat = null;
			
			int expId = 0;
			int PepId = 0;
			int test=0;
		    String insertExp = "INSERT INTO  `"+dbName+"`.`experiments_table` (`name`,`ready`,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`fractions_number` ,	`email` ,`pblication_link`,`peptide_file`,`peptides_number`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
		    
		    String insertPeptideExp = "INSERT INTO  `"+dbName+"`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
	    	
		   
		    	try {		    	
		    		if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}insertExpStat = conn.prepareStatement(insertExp,Statement.RETURN_GENERATED_KEYS); 
			  		    insertExpStat.setString(1, exp.getName().toUpperCase());
			  		    insertExpStat.setInt(2,0);   
			  		    insertExpStat.setString(3, exp.getUploadedByName().toUpperCase()); 

			  		    insertExpStat.setString(4, exp.getSpecies().toUpperCase()); 

			  		    insertExpStat.setString(5, exp.getSampleType().toUpperCase()); 

			  		    insertExpStat.setString(6, exp.getSampleProcessing().toUpperCase()); 

			  		    insertExpStat.setString(7, exp.getInstrumentType().toUpperCase()); 

			  		    insertExpStat.setString(8, exp.getFragMode().toUpperCase()); 

			  		    insertExpStat.setInt(9, 0); 			  		    
			  		    insertExpStat.setString(10, exp.getEmail().toUpperCase()); 
			  		  if(exp.getPublicationLink() != null)
		  		    		insertExpStat.setString(11, exp.getPublicationLink().toUpperCase()); 
			  		    else
			  		    	insertExpStat.setString(11, "NOT AVAILABLE"); 
			  		    insertExpStat.setInt(12,1);
			  		    insertExpStat.setInt(13,exp.getPeptidesNumber());
			  		    insertExpStat.executeUpdate();
				    	ResultSet rs  = insertExpStat.getGeneratedKeys();
				    	while(rs.next()){
				    		expId = rs.getInt(1);
				    	}
				    	for(PeptideBean pepb:exp.getPeptideList().values())
				    	{
				    		insertPeptExpStat = conn.prepareStatement(insertPeptideExp,Statement.RETURN_GENERATED_KEYS);
				    		insertPeptExpStat.setInt(1,expId); 
				    		insertPeptExpStat.executeUpdate();
					    	rs  = insertPeptExpStat.getGeneratedKeys();
					    	while(rs.next()){
					    		PepId = rs.getInt(1);

					    	} 
					    	test = this.insertPeptide(PepId, pepb,expId);
						    			    		
					    	
				    	}
				    	//conn.close();
			        
			    } catch (Exception e ) {   e.printStackTrace();return false  ;} 
			  if(test > 0 ) 
				  return true;
		}
	
		return false;
	}

	public int insertFraction(FractionBean fraction,int expId) {
		String insertFractExp = "INSERT INTO  `"+dbName+"`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`) VALUES (?,?,?) ;";
	    int fractId = -1;
	    try 
	    {	
	    	if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
			PreparedStatement insertFractExpStat = conn.prepareStatement(insertFractExp,Statement.RETURN_GENERATED_KEYS);
	    	insertFractExpStat.setInt(1,expId);
	    	insertFractExpStat.setDouble(2,fraction.getMinRange()); 
	    	insertFractExpStat.setDouble(3,fraction.getMaxRange()); 
			insertFractExpStat.executeUpdate();
	    	ResultSet rs = insertFractExpStat.getGeneratedKeys();
	    	while(rs.next()){
	    		fractId = rs.getInt(1);
	    	}				    	
	    	
	    	for(ProteinBean pb: fraction.getProteinList().values())
		    {
		    	this.insertProteinFract(fractId, pb);
		    	this.insertProt(pb.getAccession(),pb.getDescription());
		    		
		    }
	    	//conn.close();
	    } catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}
	private int insertProteinExper(String driver,String url,String dbName,String userName,String password,int expId, ProteinBean pb)
	{
		int test = -1;
		try {		    	
			if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
			String insertProtExp = "INSERT INTO  `"+dbName+"`.`experiment_protein_table` (`exp_id` ,`prot_accession` ,`other_protein(s)` ,`protein_inference_class` ,`sequence_coverage(%)` ,`observable_coverage(%)` ,`confident_ptm_sites` ,`number_confident` ,`other_ptm_sites` ,`number_other` ,`number_validated_peptides` ,`number_validated_spectra` ,`em_pai` ,`nsaf` ,`mw_(kDa)` ,`score` ,`confidence` ,`starred`)VALUES (?,?,  ?, ?, ?, ?,  ?,  ?, ?, ?, ?, ?,  ?, ?,?,?,?,?);";
			PreparedStatement	insertProtStat =  conn.prepareStatement(insertProtExp,Statement.RETURN_GENERATED_KEYS); 
			insertProtStat.setInt(1,expId);
			insertProtStat.setString(2, pb.getAccession().toUpperCase());
			insertProtStat.setString(3, pb.getOtherProteins().toUpperCase());
			insertProtStat.setString(4, pb.getProteinInferenceClass().toUpperCase());
			insertProtStat.setDouble(5,pb.getSequenceCoverage()); 
			insertProtStat.setDouble(6,pb.getObservableCoverage()); 
			insertProtStat.setString(7, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
			insertProtStat.setString(8,pb.getNumberConfident().toString()); 
			insertProtStat.setString(9, pb.getOtherPtmSites().toUpperCase());
			insertProtStat.setString(10,pb.getNumberOfOther().toUpperCase()); 
			insertProtStat.setInt(11, pb.getNumberValidatedPeptides());
			insertProtStat.setInt(12,pb.getNumberValidatedSpectra()); 
			insertProtStat.setDouble(13, pb.getEmPai());
			insertProtStat.setDouble(14,pb.getNsaf()); 
			insertProtStat.setDouble(15, pb.getMw_kDa());
			insertProtStat.setDouble(16,pb.getScore()); 
			insertProtStat.setDouble(17, pb.getConfidence());
			insertProtStat.setString(18,String.valueOf(pb.isStarred())); 
			test = insertProtStat.executeUpdate();	
			//conn.close();
		}catch(Exception e){e.printStackTrace(); return -1;}
		
		return test;
	}
	private int insertProt(String accession,String desc)//fill protein table
	{
		int test = -1;
		try {		    	
			if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
			String insertProt = "INSERT INTO  `"+dbName+"`.`proteins_table` (`accession` ,`description`)VALUES (?,?);";
			PreparedStatement	insertProtStat =  conn.prepareStatement(insertProt,Statement.RETURN_GENERATED_KEYS); 
			insertProtStat.setString(1, accession.toUpperCase());
			insertProtStat.setString(2, desc.toUpperCase());
			test = insertProtStat.executeUpdate();
			//conn.close();
		}catch(Exception e){
			test = updateProt(driver, url, dbName, userName, password, accession, desc);
		}
		return test;
	}
	private int insertProteinFract(int fractId, ProteinBean fpb)
	{
		int test = -1;
		try {		    	
			if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
			String insertProtFract = "INSERT INTO  `"+dbName+"`.`fractions_table` (`fraction_id` ,`prot_accession` ,`number_peptides` ,`number_spectra` ,`average_ precursor_intensity`)VALUES (?, ?,  ?,  ?,  ?);";
			PreparedStatement	insertProtFracStat =  conn.prepareStatement(insertProtFract,Statement.RETURN_GENERATED_KEYS); 
			insertProtFracStat.setInt(1,fractId);
			insertProtFracStat.setString(2, fpb.getAccession().toUpperCase());
			insertProtFracStat.setInt(3, fpb.getNumberOfPeptidePerFraction());
			insertProtFracStat.setInt(4,fpb.getNumberOfSpectraPerFraction());
			insertProtFracStat.setDouble(5,fpb.getAveragePrecursorIntensityPerFraction()); 
			
			test = insertProtFracStat.executeUpdate();
			//conn.close();
		}catch(Exception e){e.printStackTrace(); return -1;}
		
		return test;
		
	}
	public int insertProtDescription(String accession, String description) {
		
		int test = -1;
		try {		    	
			if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
			String insertProtDesc = "INSERT INTO  `"+dbName+"`.`proteins_table` (`accession` ,`description`)VALUES (?,?);";
			PreparedStatement	insertProtDescStat =  conn.prepareStatement(insertProtDesc); 
			insertProtDescStat.setString(1,accession.toUpperCase());
			insertProtDescStat.setString(2, description.toUpperCase());
			test = insertProtDescStat.executeUpdate();
			//conn.close();
		
		}catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e)//in case of protein existence so update description
		{
		
			try{
				if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					} 
				String updateProtDesc = "UPDATE  `"+dbName+"`.`proteins_table` SET `description`=? WHERE `accession` = ? ;";
				PreparedStatement	updateProtDescStat =  conn.prepareStatement(updateProtDesc); 
				updateProtDescStat.setString(2,accession.toUpperCase());
				updateProtDescStat.setString(1, description.toUpperCase());
				test = updateProtDescStat.executeUpdate();
			}catch(Exception e2){e2.printStackTrace();}
		
		
		
		}
		catch(Exception e){e.printStackTrace();}
		
		return test;
	}
	public int insertPeptide(int pepId,PeptideBean pepb,int expId) {
		   String insertPeptide = "INSERT INTO  `"+dbName+"`.`proteins_peptides_table` (`protein` ,`other_protein(s)` ,`peptide_protein(s)` ,`other_protein_description(s)` ,`peptide_proteins_description(s)` ,`aa_before` ,`sequence` ,"
		    						+"`aa_after` ,`peptide_start` ,`peptide_end` ,`variable_modification` ,`location_confidence` ,`precursor_charge(s)` ,`number_of_validated_spectra` ,`score` ,`confidence` ,`peptide_id`)VALUES ("
		    						+"?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?);";
		   if(pepId == -1)//generate peptide id
		   {
			   String insertPeptideExp = "INSERT INTO  `"+dbName+"`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
			   try {		    	
				   if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				   PreparedStatement insertPeptExpStat = conn.prepareStatement(insertPeptideExp,Statement.RETURN_GENERATED_KEYS);
		    		insertPeptExpStat.setInt(1,expId); 
		    		insertPeptExpStat.executeUpdate();
			    	ResultSet rs = insertPeptExpStat.getGeneratedKeys();
			    	while(rs.next()){
			    		pepId = rs.getInt(1);	
			    	}
			    	//conn.close();
			   }catch(Exception e){e.printStackTrace();}
		   }
		    
		    int test = -1;
			try {		    	
				 if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				PreparedStatement	insertPeptideStat =  conn.prepareStatement(insertPeptide,Statement.RETURN_GENERATED_KEYS); 
				insertPeptideStat.setString(1, pepb.getProtein().toUpperCase());
				insertPeptideStat.setString(2, pepb.getOtherProteins().toUpperCase());
				insertPeptideStat.setString(3, pepb.getPeptideProteins().toUpperCase());
				insertPeptideStat.setString(4, pepb.getOtherProteinDescriptions().toUpperCase());						
				insertPeptideStat.setString(5, pepb.getPeptideProteinsDescriptions().toUpperCase());
				insertPeptideStat.setString(6, pepb.getAaBefore().toUpperCase());
				insertPeptideStat.setString(7, pepb.getSequence().toUpperCase());
				insertPeptideStat.setString(8, pepb.getAaAfter().toUpperCase());
				insertPeptideStat.setString(9, pepb.getPeptideStart().toUpperCase());
				insertPeptideStat.setString(10, pepb.getPeptideEnd().toUpperCase());
				insertPeptideStat.setString(11, pepb.getVariableModification().toUpperCase());
				insertPeptideStat.setString(12, pepb.getLocationConfidence().toUpperCase());
				insertPeptideStat.setString(13, pepb.getPrecursorCharges().toUpperCase());
				insertPeptideStat.setInt(14, pepb.getNumberOfValidatedSpectra());
				insertPeptideStat.setDouble(15, pepb.getScore());
				insertPeptideStat.setDouble(16, pepb.getConfidence());
				insertPeptideStat.setInt(17, pepId);
				test = insertPeptideStat.executeUpdate();
				
				//conn.close();	
			}catch(Exception e){e.printStackTrace();
			}
			return test;
	}

	public boolean addProtine(ProteinBean pb, int expId) {
		
		 int test = this.insertProteinExper(driver, url, dbName, userName, password, expId, pb);
		 test = this.insertProt(pb.getAccession(),pb.getDescription());
		 if(test >0)
			 return true;
		
		return false;
	}
	private int updateProt(String driver,String url,String dbName,String userName,String password,String accession,String desc)//fill protein table
	{
		int test = -1;
		try {		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			     String insertProt = "UPDATE  `"+dbName+"`.`proteins_table` SET `description` = ? WHERE `accession`=?;";
			PreparedStatement	insertProtStat =  conn.prepareStatement(insertProt,Statement.RETURN_GENERATED_KEYS); 
			insertProtStat.setString(1, desc.toUpperCase());
			insertProtStat.setString(2, accession.toUpperCase());			
			test = insertProtStat.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		return test;
	}
	public int updateFractionNumber(int expId, int fractionsNumber) {
		int test = -1;
		try {		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			     	String updateFractionNumber = "UPDATE  `experiments_table` SET `fractions_number` = ? WHERE WHERE `exp_id` = ?;";
			PreparedStatement	updateFractStat =  conn.prepareStatement(updateFractionNumber); 
			updateFractStat.setInt(1, fractionsNumber);
			updateFractStat.setInt(2, expId);			
			test = updateFractStat.executeUpdate();
			//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return test;
		
	}
	public boolean updateExperiment( ExperimentBean exp)
	{
		PreparedStatement updateExperStat = null;
		try{
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			    String updateExp = "UPDATE `experiments_table`  SET  `name`=? ,`fractions_number`=? ,`ready` =?,`uploaded_by`=?, `peptide_file`=?,`pblication_link`=?, `peptides_number`=? ,`proteins_number`=? WHERE  `exp_id`=? ";
			    updateExperStat =  conn.prepareStatement(updateExp); 
			
					
			
			updateExperStat.setString(1, exp.getName().toUpperCase());
			updateExperStat.setInt(2, exp.getFractionsNumber());
			updateExperStat.setInt(3,exp.getReady()); 			
			updateExperStat.setString(4, exp.getUploadedByName().toUpperCase());			
			updateExperStat.setInt(5,exp.getPeptidesInclude());		
			updateExperStat.setString(6,exp.getPublicationLink());
			updateExperStat.setInt(7,exp.getPeptidesNumber());
			updateExperStat.setInt(8,exp.getProteinsNumber());
			updateExperStat.setInt(9,exp.getExpId()); 	
			int test = updateExperStat.executeUpdate();
			//conn.close();
			if(test > 0)
				return true;
			return false;
		}catch(Exception e){e.printStackTrace();return false;}
		
	}
	public boolean updateProtein(ProteinBean pb,int expId)
	{
		PreparedStatement updateProtStat = null;
		try{
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   String updateProt = "UPDATE `"+dbName+"`.`experiment_protein_table` SET  `other_protein(s)`=? ,`protein_inference_class`=? ,`sequence_coverage(%)` =?,`observable_coverage(%)`=? ,`confident_ptm_sites`=? ,`number_confident` =?,`other_ptm_sites`=? ,`number_other`=? ,`number_validated_peptides` =?,`number_validated_spectra`=? ,`em_pai` =?,`nsaf` =?,`mw_(kDa)`=? ,`score` =?,`confidence`=? ,`starred`=? WHERE  `exp_id`=? AND `prot_accession`=?";
			updateProtStat =  conn.prepareStatement(updateProt,Statement.RETURN_GENERATED_KEYS); 
			updateProtStat.setString(1, pb.getOtherProteins().toUpperCase());
			updateProtStat.setString(2, pb.getProteinInferenceClass().toUpperCase());
			updateProtStat.setDouble(3,pb.getSequenceCoverage()); 
			updateProtStat.setDouble(4,pb.getObservableCoverage()); 
			updateProtStat.setString(5, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
			updateProtStat.setString(6,pb.getNumberConfident().toString()); 
			updateProtStat.setString(7, pb.getOtherPtmSites().toUpperCase());
			updateProtStat.setString(8,pb.getNumberOfOther().toUpperCase()); 
			updateProtStat.setInt(9, pb.getNumberValidatedPeptides());
			updateProtStat.setInt(10,pb.getNumberValidatedSpectra()); 
			updateProtStat.setDouble(11, pb.getEmPai());
			updateProtStat.setDouble(12,pb.getNsaf()); 
			updateProtStat.setDouble(13, pb.getMw_kDa());
			updateProtStat.setDouble(14,pb.getScore()); 
			updateProtStat.setDouble(15, pb.getConfidence());
			updateProtStat.setString(16,String.valueOf(pb.isStarred())); 
			updateProtStat.setInt(17,expId);
			updateProtStat.setString(18, pb.getAccession().toUpperCase());			
			int test = updateProtStat.executeUpdate();
			test = this.updateProt(driver, url, dbName, userName, password, pb.getAccession(),pb.getDescription());
			//conn.close();
			if(test > 0)
				return true;
			else
				return false;
		}catch(Exception e){e.printStackTrace();return false;}
		
	}
	public boolean updatePeptide(PeptideBean pepb) {
		try {		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   String updatePeptideNumber = "UPDATE  `proteins_peptides_table` SET `protein`=? ,`other_protein(s)`=? ,`peptide_protein(s)`=? ,`other_protein_description(s)`=? ,`peptide_proteins_description(s)`=? ,`aa_before`=? ,`sequence`=? ,"
		    						+"`aa_after`=? ,`peptide_start`=? ,`peptide_end`=? ,`variable_modification`=? ,`location_confidence`=? ,`precursor_charge(s)`=? ,`number_of_validated_spectra`=? ,`score`=? ,`confidence`=?   WHERE `sequence` = ?;";
			PreparedStatement	insertPeptideStat =  conn.prepareStatement(updatePeptideNumber); 
			insertPeptideStat.setString(1, pepb.getProtein().toUpperCase());
			insertPeptideStat.setString(2, pepb.getOtherProteins().toUpperCase());
			insertPeptideStat.setString(3, pepb.getPeptideProteins().toUpperCase());
			insertPeptideStat.setString(4, pepb.getOtherProteinDescriptions().toUpperCase());						
			insertPeptideStat.setString(5, pepb.getPeptideProteinsDescriptions().toUpperCase());
			insertPeptideStat.setString(6, pepb.getAaBefore().toUpperCase());
			insertPeptideStat.setString(7, pepb.getSequence().toUpperCase());
			insertPeptideStat.setString(8, pepb.getAaAfter().toUpperCase());
			insertPeptideStat.setString(9, pepb.getPeptideStart().toUpperCase());
			insertPeptideStat.setString(10, pepb.getPeptideEnd().toUpperCase());
			insertPeptideStat.setString(11, pepb.getVariableModification().toUpperCase());
			insertPeptideStat.setString(12, pepb.getLocationConfidence().toUpperCase());
			insertPeptideStat.setString(13, pepb.getPrecursorCharges().toUpperCase());
			insertPeptideStat.setInt(14, pepb.getNumberOfValidatedSpectra());
			insertPeptideStat.setDouble(15, pepb.getScore());
			insertPeptideStat.setDouble(16, pepb.getConfidence());
			insertPeptideStat.setString(17, pepb.getSequence());			
			insertPeptideStat.executeUpdate();
			//conn.close();
		}catch(Exception e){e.printStackTrace();return false;}
		return true;
		
	}
	public boolean updateFractions(FractionBean fb, int expId)
	{
		List<Integer> fractionIDs = this.getFractionList(expId);
		for(int fractId:fractionIDs)
		{			
			try {		    	
				 if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				   String updateFraction = "UPDATE  `"+dbName+"`.`fractions_table` SET `number_peptides`=? ,`number_spectra`=? ,`average_ precursor_intensity`=?   WHERE `fraction_id` = ? AND `prot_accession`=?;";
				PreparedStatement	updateFractionStat=null;				
				for(ProteinBean fpb:fb.getProteinList().values()){
					boolean test = this.checkFractionProtine(fractId,fpb.getAccession());
					if(test)
					{					
						updateFractionStat =  conn.prepareStatement(updateFraction);			
						updateFractionStat.setInt(1, fpb.getNumberOfPeptidePerFraction());
						updateFractionStat.setInt(2,fpb.getNumberOfSpectraPerFraction());
						updateFractionStat.setDouble(3,fpb.getAveragePrecursorIntensityPerFraction()); 
						updateFractionStat.setInt(4,fb.getFractionId());
						updateFractionStat.setString(5, fpb.getAccession().toUpperCase());
						updateFractionStat.executeUpdate();
					}
					else
						this.insertFraction(fb, expId);
				
				}
				//conn.close();
			}catch(Exception e){e.printStackTrace();return false;}
			
		}
		
		return false;
		
		
	}
	
	private boolean checkFractionProtine(int fractId, String accession) {
		PreparedStatement selectFractStat = null;
		
	    String selectFractProt = "SELECT `number_spectra` FROM `fractions_table` WHERE `fraction_id`=? AND `prot_accession`=?;";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectFractStat = conn.prepareStatement(selectFractProt); 
			selectFractStat.setInt(1,fractId);
			selectFractStat.setString(2,accession);
	  		ResultSet rs = selectFractStat.executeQuery();
	  		while(rs.next())
	  		{
	  			
	  			return true;
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
		return false;
	}
	public boolean checkProteinExisting(int expId, String prot_accession )//check if the protein exist so we use update 
	{
		PreparedStatement selectExpStat = null;
		boolean test=false;
	    String selectExpProt = "SELECT `exp_id`,`prot_accession` FROM `experiment_protein_table` where `exp_id`=? and `prot_accession` = ?";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectExpStat = conn.prepareStatement(selectExpProt); 
  		    selectExpStat.setInt(1,expId);
	  		selectExpStat.setString(2,prot_accession);
	  		ResultSet rs = selectExpStat.executeQuery();
	  		while(rs.next())
	  		{
	  			test = true;
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
		return test;
	}
	public ExperimentBean readyExper(int expId)
	{
		PreparedStatement selectExpStat = null;
		
	    String selectExpProt = "SELECT * FROM `experiments_table` WHERE `exp_id` = ?";
		
		ExperimentBean exp = new ExperimentBean();
		
	    try 
	    {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectExpStat = conn.prepareStatement(selectExpProt); 
  		    selectExpStat.setInt(1,expId);
	  		ResultSet rs = selectExpStat.executeQuery();
	  		while(rs.next())
	  		{
	  			
	  			int ready = rs.getInt("ready");
	  			exp.setReady(ready);
	  			int fractionsNumber= rs.getInt("fractions_number");
	  			exp.setFractionsNumber(fractionsNumber);
	  			String uploadedBy = rs.getString("uploaded_by");
	  			exp.setUploadedByName(uploadedBy);
	  			String name = rs.getString("name");
	  			exp.setName(name);
	  			String species= rs.getString("species");
	  			exp.setSpecies(species);
	  			String sampleType=rs.getString("sample_type");
	  			exp.setSampleType(sampleType);
	  			String sampleProcessing=rs.getString("sample_processing");
	  			exp.setSampleProcessing(sampleProcessing);
	  			String instrumentType=rs.getString("instrument_type");
	  			exp.setInstrumentType(instrumentType);
	  			String fragMode=rs.getString("frag_mode");
	  			exp.setFragMode(fragMode);
	  			int proteinsNumber=rs.getInt("proteins_number");
	  			exp.setProteinsNumber(proteinsNumber);
	  			String email=rs.getString("email");
	  			exp.setEmail(email);
	  			String publicationLink=rs.getString("pblication_link");
	  			exp.setPublicationLink(publicationLink);
	  			int peptidesInclude = rs.getInt("peptide_file");
	  			exp.setPeptidesInclude(peptidesInclude);
	  			int peptidesNumber = rs.getInt("peptides_number");
	  			exp.setPeptidesNumber(peptidesNumber);
	  			exp.setExpId(expId);
	  			
	  			
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();return null;}//error
	    return exp;
	   
	    	
	    	
		
	}
	public int getFractionNumber(int expId) {
		
		PreparedStatement selectExpStat = null;
		int  fractionNumber=0;
	    String selectExp = "SELECT `fractions_number` FROM `experiments_table` WHERE `exp_id` = ?;";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			      selectExpStat = conn.prepareStatement(selectExp); 
  		    selectExpStat.setInt(1,expId);
	  		ResultSet rs = selectExpStat.executeQuery();
	  		while(rs.next())
	  		{ 
	  			fractionNumber = rs.getInt("fractions_number");
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
		return fractionNumber;
	}
	public boolean checkPeptideExisting(String sequence) {
		PreparedStatement selectExpStat = null;
		boolean test=false;
	    String selectExpProt = "SELECT `sequence` FROM `proteins_peptides_table` WHERE `sequence`=?";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectExpStat = conn.prepareStatement(selectExpProt); 
  		    selectExpStat.setString(1,sequence);
	  		ResultSet rs = selectExpStat.executeQuery();
	  		while(rs.next())
	  		{
	  			test = true;
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
		return test;
	}
	
		// RETRIVEING DATA
	
	public Map<Integer,ExperimentBean> getExperiments()//get experiments list
	{
		PreparedStatement selectExpListStat = null;
		Map<Integer,ExperimentBean> ExpList = new HashMap<Integer, ExperimentBean>();
	    String selectselectExpList = "SELECT * FROM `experiments_table` ;";
		
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			    selectExpListStat = conn.prepareStatement(selectselectExpList); 
  		   	ResultSet rs = selectExpListStat.executeQuery();
	  		while(rs.next())
	  		{
	  			ExperimentBean exp = new ExperimentBean();
	  			int ready = rs.getInt("ready");
	  			exp.setReady(ready);
	  			int fractionsNumber= rs.getInt("fractions_number");
	  			exp.setFractionsNumber(fractionsNumber);
	  			String uploadedBy = rs.getString("uploaded_by");
	  			exp.setUploadedByName(uploadedBy);
	  			String name = rs.getString("name");
	  			exp.setName(name);
	  			String species= rs.getString("species");
	  			exp.setSpecies(species);
	  			String sampleType=rs.getString("sample_type");
	  			exp.setSampleType(sampleType);
	  			String sampleProcessing=rs.getString("sample_processing");
	  			exp.setSampleProcessing(sampleProcessing);
	  			String instrumentType=rs.getString("instrument_type");
	  			exp.setInstrumentType(instrumentType);
	  			String fragMode=rs.getString("frag_mode");
	  			exp.setFragMode(fragMode);
	  			int proteinsNumber=rs.getInt("proteins_number");
	  			exp.setProteinsNumber(proteinsNumber);
	  			String email=rs.getString("email");
	  			exp.setEmail(email);
	  			String publicationLink=rs.getString("pblication_link");
	  			exp.setPublicationLink(publicationLink);
	  			int peptidesInclude = rs.getInt("peptide_file");
	  			exp.setPeptidesInclude(peptidesInclude);
	  			int peptidesNumber = rs.getInt("peptides_number");
	  			exp.setPeptidesNumber(peptidesNumber);
	  			int expId = rs.getInt("exp_id");
	  			exp.setExpId(expId);
	  			ExpList.put(exp.getExpId(),exp);
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();return null;}//error
	    return ExpList;
		
		
	}
	public ExperimentBean getExperiment(int expId) {
		ExperimentBean exp = new ExperimentBean();
		exp.setExpId(expId);
		exp = this.getExpDetails(exp);
		exp.setFractionsList(this.getFractionsList(exp.getExpId()));
		exp.setProteinList(this.getExpProteinsList(expId));	   	//get protein details	
		exp.setPeptideList(this.getExpPeptides(expId));
	    return exp;
	}
	private List<Integer> getFractionList(int expId) {
		PreparedStatement selectExpFractionStat = null;
		String selectExpFraction = "SELECT `fraction_id` FROM `experiment_fractions_table` WHERE `exp_id`=?;";
		List<Integer> fractionList = new ArrayList<Integer>();
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectExpFractionStat = conn.prepareStatement(selectExpFraction); 
			selectExpFractionStat.setInt(1,expId);
	  		ResultSet rs = selectExpFractionStat.executeQuery();
	  		while(rs.next())
	  		{
	  			fractionList.add(rs.getInt("fraction_id"));
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
		return fractionList;
	}
	private ExperimentBean getExpDetails(ExperimentBean exp)
	{
		PreparedStatement selectExpStat = null;	
	    String selectExp = "SELECT * FROM `experiments_table` WHERE `exp_id`=? ;";
		try
		{		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectExpStat = conn.prepareStatement(selectExp); 
			selectExpStat.setInt(1,exp.getExpId());
  		   	ResultSet rs = selectExpStat.executeQuery();
	  		while(rs.next())
	  		{
	  			int ready = rs.getInt("ready");
	  			exp.setReady(ready);
	  			int fractionsNumber= rs.getInt("fractions_number");
	  			exp.setFractionsNumber(fractionsNumber);
	  			String uploadedBy = rs.getString("uploaded_by");
	  			exp.setUploadedByName(uploadedBy);
	  			String name = rs.getString("name");
	  			exp.setName(name);
	  			String species= rs.getString("species");
	  			exp.setSpecies(species);
	  			String sampleType=rs.getString("sample_type");
	  			exp.setSampleType(sampleType);
	  			String sampleProcessing=rs.getString("sample_processing");
	  			exp.setSampleProcessing(sampleProcessing);
	  			String instrumentType=rs.getString("instrument_type");
	  			exp.setInstrumentType(instrumentType);
	  			String fragMode=rs.getString("frag_mode");
	  			exp.setFragMode(fragMode);
	  			int proteinsNumber=rs.getInt("proteins_number");
	  			exp.setProteinsNumber(proteinsNumber);
	  			String email=rs.getString("email");
	  			exp.setEmail(email);
	  			String publicationLink=rs.getString("pblication_link");
	  			exp.setPublicationLink(publicationLink);
	  			int peptidesInclude = rs.getInt("peptide_file");
	  			exp.setPeptidesInclude(peptidesInclude);
	  			int peptidesNumber = rs.getInt("peptides_number");
	  			exp.setPeptidesNumber(peptidesNumber);
	  		}
	  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
	  		return exp;
		
	}
	public Map<Integer,FractionBean> getFractionsList( int expId)
	{
		Map<Integer,FractionBean> fractionsList = new HashMap<Integer, FractionBean>();		  	
		try
		{		    	
	    	
	  		//get fractions id list
	  		PreparedStatement selectFractsListStat = null;		
	  		double minRange = 0.0;
	  		double maxRange = 0.0;
		    String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range` FROM `experiment_fractions_table` where `exp_id` = ?";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		    selectFractsListStat = conn.prepareStatement(selectFractList); 
		    selectFractsListStat.setInt(1,expId);
	  		ResultSet rs = selectFractsListStat.executeQuery();
	  		ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int fraction_id= rs.getInt("fraction_id");
 		  		minRange = rs.getDouble("min_range");
 		  		maxRange = rs.getDouble("max_range");
 		  		fractionIdList.add(fraction_id);
		  		
 		  	}  		
		  		
		  		//get fractions 
		  		PreparedStatement selectFractsStat = null;	
		  		String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ?";
	  			
		  		for(int fractId:fractionIdList)
		  		{

			  		FractionBean fb = new FractionBean();
			  		fb.setFractionId(fractId);
			  		fb.setMinRange(minRange);
			  		fb.setMaxRange(maxRange);
			  		 if(conn == null || conn.isClosed()){
							Class.forName(driver).newInstance();
							conn = DriverManager.getConnection(url+dbName, userName, password);
							}
					   selectFractsStat = conn.prepareStatement(selectFract); 
			    	selectFractsStat.setInt(1,fractId);
		  		   	rs = selectFractsStat.executeQuery();
		  		   	Map<String,ProteinBean> proteinList = new HashMap<String, ProteinBean>();
		  		   	
		  		   	while(rs.next())
			  		{
		  		   		ProteinBean pb = new ProteinBean();//fraction_id		  			
			  			pb.setAccession(rs.getString("prot_accession"));
			  			pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
			  			pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
			  			pb.setAveragePrecursorIntensityPerFraction(rs.getDouble("average_ precursor_intensity"));
			  			proteinList.put(pb.getAccession(),pb);			  			
			  		}  

		  		   	fb.setProteinList(proteinList);
		  		   	fractionsList.put(fb.getFractionId(),fb);
		  		   	
		  		}
		  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return fractionsList;
		
		
	}
	public Map<Integer,PeptideBean> getExpPeptides(int expId)
	{

		Map<Integer,PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();		  	
		try
		{		    	
	    	
	  		//get fractions id list
	  		PreparedStatement selectPeptideListStat = null;				
		    String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		   selectPeptideListStat = conn.prepareStatement(selectPeptideList); 
		    selectPeptideListStat.setInt(1,expId);
	  		ResultSet rs = selectPeptideListStat.executeQuery();
	  		ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int peptideId= rs.getInt("pep_id");
 		  		peptideIdList.add(peptideId);
		  		
 		  	}  		
	  		
		  		
		  		//get fractions 
		  		PreparedStatement selectPeptidesStat = null;	
		  		String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE `peptide_id` = ?;";
	  			
		  		for(int pepId:peptideIdList)
		  		{

			  		PeptideBean pepb = new PeptideBean();
			  		pepb.setPeptideId(pepId);
			  		 if(conn == null || conn.isClosed()){
							Class.forName(driver).newInstance();
							conn = DriverManager.getConnection(url+dbName, userName, password);
							}
					   selectPeptidesStat = conn.prepareStatement(selectPeptide); 
			    	selectPeptidesStat.setInt(1,pepId);
		  		   	rs = selectPeptidesStat.executeQuery();
		  		   	
		  		   	while(rs.next())
			  		{		  		
				  		   	pepb.setAaAfter(rs.getString("aa_after"));
				  		   	pepb.setAaBefore(rs.getString("aa_before"));
					  		pepb.setConfidence(rs.getDouble("confidence"));
					  		pepb.setLocationConfidence(rs.getString("location_confidence"));
					  		pepb.setNumberOfValidatedSpectra(rs.getInt("number_of_validated_spectra"));
					  		pepb.setOtherProteinDescriptions(rs.getString("other_protein_description(s)"));
					  		pepb.setOtherProteins(rs.getString("other_protein(s)"));
					  		pepb.setPeptideEnd(rs.getString("peptide_end"));
					  		pepb.setPeptideProteins((rs.getString("peptide_protein(s)")));
					  		pepb.setPeptideProteinsDescriptions(rs.getString("peptide_proteins_description(s)"));
					  		pepb.setPeptideStart(rs.getString("peptide_start"));
					  		pepb.setPrecursorCharges(rs.getString("precursor_charge(s)"));
					  		pepb.setProtein(rs.getString("protein"));
					  		pepb.setScore(rs.getDouble("score"));
					  		pepb.setSequence(rs.getString("sequence"));
					  		pepb.setVariableModification(rs.getString("variable_modification"));	
					  		pepb.setPeptideId(pepId);
				  			peptidesList.put(pepb.getPeptideId(),pepb);			  			
			  		}  
		  		   	
		  		}
		  		//conn.close();  	
		}catch(Exception e){e.printStackTrace();}		
		return peptidesList;
	}
	
	public Map<String,ProteinBean> getExpProteinsList(int expId) {
		Map<String,ProteinBean> proteinExpList = new HashMap<String, ProteinBean>();  		
		try{
	  	 	PreparedStatement selectProtExpStat = null;	
	  		String selectProtExp = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? ;";
	  		 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectProtExpStat = conn.prepareStatement(selectProtExp); 
	    	selectProtExpStat.setInt(1,expId);
  		   	ResultSet rs = selectProtExpStat.executeQuery();
  		   while(rs.next())
	  		{
  			   	ProteinBean temPb = new ProteinBean();
  			   	temPb.setAccession(rs.getString("prot_accession"));
	  			temPb.setOtherProteins(rs.getString("other_protein(s)"));
	  			temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
	  			temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
	  			temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
	  			temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
	  			temPb.setNumberConfident(rs.getString("number_confident"));
	  			temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
	  			temPb.setNumberOfOther(rs.getString("number_other"));
	  			temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
	  			temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
	  			temPb.setEmPai(rs.getDouble("em_pai"));
	  			temPb.setNsaf(rs.getDouble("nsaf"));
	  			temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
	  			temPb.setScore(rs.getDouble("score"));
	  			temPb.setConfidence(rs.getDouble("confidence"));
	  			temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
	  			proteinExpList.put(temPb.getAccession(), temPb);
	  		} 			  		   	  
	  		   	
	  		   	PreparedStatement selectProtStat = null;	
		  		String selectProt = "SELECT   `description` FROM `proteins_table` WHERE `accession`=?";
	  		   	Map<String,ProteinBean> temProteinList = new HashMap<String, ProteinBean>();
	  		   	for(ProteinBean temPb: proteinExpList.values())
	  		   	{
	  		   	 if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				   selectProtStat = conn.prepareStatement(selectProt); 
			    	selectProtStat.setString(1,temPb.getAccession().toUpperCase());
		  		   	rs = selectProtStat.executeQuery();
		  		   while(rs.next())
			  		{
		  		   					
			  			temPb.setDescription(rs.getString("description"));
			  			temProteinList.put(temPb.getAccession(), temPb);
			  		}
		  		   	  		   		
	  		   	}
	  		  proteinExpList.clear();
	  		  proteinExpList.putAll(temProteinList);
	  		  temProteinList.clear();
	  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return proteinExpList;
	}

	//REMOVE DATA
	public boolean removeExperiment(int expId) {
		
		PreparedStatement remExpStat = null;//done
		PreparedStatement getFractExpStat = null;//done
		PreparedStatement remFractStat = null;//done
		PreparedStatement remFractExpStat = null;//done
		PreparedStatement getPepExpStat = null;//done
		PreparedStatement remPepExpStat = null;//done
		PreparedStatement remPeptStat = null;//done
		PreparedStatement remProtStat = null;//done
		try{
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   String remExp = "DELETE FROM `experiments_table`  WHERE  `exp_id`=? ";
			  String remExpPro = "DELETE FROM `"+dbName+"`.`experiment_protein_table`  WHERE  `exp_id`=? ";
			  
			  String remFract = "DELETE FROM `"+dbName+"`.`fractions_table`   WHERE  `fraction_id` =? ";
			
			remExpStat =  conn.prepareStatement(remExp); 
			remExpStat.setInt(1, expId);
			remExpStat.executeUpdate();
			
			String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   getPepExpStat = conn.prepareStatement(selectPeptideList); 
		    getPepExpStat.setInt(1,expId);
	  		ResultSet rs = getPepExpStat.executeQuery();
	  		ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int peptideId= rs.getInt("pep_id");
 		  		peptideIdList.add(peptideId);
		  		
 		  	}  		
		  		
		  		
		  		String selectPeptide = "DELETE FROM `proteins_peptides_table` WHERE  `peptide_id`=? ;";
	  			
		  		for(int pepId:peptideIdList)
		  		{

			  		
		  			 if(conn == null || conn.isClosed()){
							Class.forName(driver).newInstance();
							conn = DriverManager.getConnection(url+dbName, userName, password);
							}
					   remPeptStat = conn.prepareStatement(selectPeptide); 
			    	remPeptStat.setInt(1,pepId);
		  		   	remPeptStat.executeUpdate();	  		   	
		  		   	
		  		}
		  		
		  		String removePeptide = "DELETE FROM `experiment_peptides_table` WHERE  `exp_id` = ? ;";
		  		 if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				   remPepExpStat = conn.prepareStatement(removePeptide); 
		    	remPepExpStat.setInt(1,expId);
		    	remPepExpStat.executeUpdate();	  	
	  						
			
		    remProtStat =  conn.prepareStatement(remExpPro); 
		    remProtStat.setInt(1, expId);
			remExpStat.executeUpdate();
			
			
							
		    String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		   getFractExpStat = conn.prepareStatement(selectFractList); 
		    getFractExpStat.setInt(1,expId);
	  		rs = getFractExpStat.executeQuery();
	  		ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int fraction_id= rs.getInt("fraction_id");
 		  		fractionIdList.add(fraction_id);
		  		
 		  	} 		
			
	  			  		
			for(int fb:fractionIdList)
			{
				remFractStat =  conn.prepareStatement(remFract); 
				remFractStat.setInt(1, fb);
				remExpStat.executeUpdate();
			}
			
			
			String removeFraction = "DELETE FROM `experiment_fractions_table`   WHERE `exp_id` = ? ;";
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   remFractExpStat = conn.prepareStatement(removeFraction); 
	    	remFractExpStat.setInt(1,expId);
	    	remFractExpStat.executeUpdate();
	    	//conn.close();
			return true;
		}catch(Exception e){e.printStackTrace();return false;}
	}

	public ProteinBean searchProtein(String accession,int expId) {
		PreparedStatement selectProStat = null;
		boolean test=false;
	    String selectPro = "SELECT `starred` FROM `experiment_protein_table` Where `exp_id`=? AND `prot_accession`=?;";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			selectProStat = conn.prepareStatement(selectPro); 
			selectProStat.setString(2,accession);
			selectProStat.setInt(1, expId);
	  		ResultSet rs = selectProStat.executeQuery();
	  		while(rs.next())
	  		{
	  			test = true;
	  		}
	  		if(test)
	  		{
	  			ProteinBean pb = this.getProtein(accession,expId);
	  			return pb;
	  		}
	  		else
	  		{
	  			return null;
	  		}
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();return null;}
	    
		
	}

	private ProteinBean getProtein(String accession, int expId) {
		try{
	  	 	PreparedStatement selectProtExpStat = null;	
	  		String selectProtExp = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? AND `prot_accession`=?;";
	  		 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectProtExpStat = conn.prepareStatement(selectProtExp); 
	    	selectProtExpStat.setInt(1,expId);
	    	selectProtExpStat.setString(2,accession);
  		   	ResultSet rs = selectProtExpStat.executeQuery();
  		   	ProteinBean temPb = new ProteinBean();
  		   while(rs.next())
	  		{
  			   	
  			   	temPb.setAccession(accession);
	  			temPb.setOtherProteins(rs.getString("other_protein(s)"));
	  			temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
	  			temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
	  			temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
	  			temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
	  			temPb.setNumberConfident(rs.getString("number_confident"));
	  			temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
	  			temPb.setNumberOfOther(rs.getString("number_other"));
	  			temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
	  			temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
	  			temPb.setEmPai(rs.getDouble("em_pai"));
	  			temPb.setNsaf(rs.getDouble("nsaf"));
	  			temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
	  			temPb.setScore(rs.getDouble("score"));
	  			temPb.setConfidence(rs.getDouble("confidence"));
	  			temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
	  			 //proteinList.remove(temPb.getAccession());
	  		} 			  		   	  
	  		   	
	  		   	PreparedStatement selectProtStat = null;	
		  		String selectProt = "SELECT   `description` FROM `proteins_table` WHERE `accession`= ? ";
		  		 if(conn == null || conn.isClosed()){
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url+dbName, userName, password);
						}
				   selectProtStat = conn.prepareStatement(selectProt); 
			    selectProtStat.setString(1,accession.toUpperCase());
		  		rs = selectProtStat.executeQuery();
		  		while(rs.next())
			  	{
		  		   					
			  			temPb.setDescription(rs.getString("description"));
			  	}
		  		//conn.close();
	  		return temPb;
		}catch(Exception e){e.printStackTrace();}
		return null;
	}

	public Map<Integer, PeptideBean> getPeptidesProtList(int expId,	String accession) {


		Map<Integer,PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();		  	
		try
		{		    	
	    	
	  		//get fractions id list
	  		PreparedStatement selectPeptideListStat = null;				
		    String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		    selectPeptideListStat = conn.prepareStatement(selectPeptideList); 
		    selectPeptideListStat.setInt(1,expId);
	  		ResultSet rs = selectPeptideListStat.executeQuery();
	  		ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int peptideId= rs.getInt("pep_id");
 		  		peptideIdList.add(peptideId);
		  		
 		  	}  		
		  		
		  		//get fractions 
		  		PreparedStatement selectPeptidesStat = null;	
		  		String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE  `peptide_id`=? AND  `protein` =? ;";
	  			
		  		for(int pepId:peptideIdList)
		  		{

			  		PeptideBean pepb = new PeptideBean();
			  		pepb.setPeptideId(pepId);
			  		 if(conn == null || conn.isClosed()){
							Class.forName(driver).newInstance();
							conn = DriverManager.getConnection(url+dbName, userName, password);
							}
					   selectPeptidesStat = conn.prepareStatement(selectPeptide); 
			    	selectPeptidesStat.setInt(1,pepId);
			    	selectPeptidesStat.setString(2,accession);
		  		   	rs = selectPeptidesStat.executeQuery();
		  		   	
		  		   	while(rs.next())
			  		{		  		
				  		   	pepb.setAaAfter(rs.getString("aa_after"));
				  		   	pepb.setAaBefore(rs.getString("aa_before"));
					  		pepb.setConfidence(rs.getDouble("confidence"));
					  		pepb.setLocationConfidence(rs.getString("location_confidence"));
					  		pepb.setNumberOfValidatedSpectra(rs.getInt("number_of_validated_spectra"));
					  		pepb.setOtherProteinDescriptions(rs.getString("other_protein_description(s)"));
					  		pepb.setOtherProteins(rs.getString("other_protein(s)"));
					  		pepb.setPeptideEnd(rs.getString("peptide_end"));
					  		pepb.setPeptideProteins((rs.getString("peptide_protein(s)")));
					  		pepb.setPeptideProteinsDescriptions(rs.getString("peptide_proteins_description(s)"));
					  		pepb.setPeptideStart(rs.getString("peptide_start"));
					  		pepb.setPrecursorCharges(rs.getString("precursor_charge(s)"));
					  		pepb.setProtein(rs.getString("protein"));
					  		pepb.setScore(rs.getDouble("score"));
					  		pepb.setSequence(rs.getString("sequence"));
					  		pepb.setVariableModification(rs.getString("variable_modification"));	
					  		pepb.setPeptideId(pepId);
				  			peptidesList.put(pepb.getPeptideId(),pepb);			  			
			  		}  
		  		   	
		  		}
		  		//conn.close();
		}catch(Exception e){e.printStackTrace();}	
		return peptidesList;
	}

	public Map<Integer, FractionBean> getProteinFractionList(String accession,	int expId) {
		Map<Integer,FractionBean> fractionsList = new HashMap<Integer, FractionBean>();		  	
		try
		{		    	
	    	
	  		//get fractions id list
	  		PreparedStatement selectFractsListStat = null;
	  		double minRange = 0.0;
	  		double maxRange = 0.0;
		    String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range` FROM `experiment_fractions_table` where `exp_id` = ?";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		    selectFractsListStat = conn.prepareStatement(selectFractList); 
		    selectFractsListStat.setInt(1,expId);
	  		ResultSet rs = selectFractsListStat.executeQuery();
	  		ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
	  		while(rs.next())
 		  	{
 		  		int fraction_id= rs.getInt("fraction_id");
 		  		minRange = rs.getDouble("min_range");
 		  		maxRange = rs.getDouble("max_range");
 		  		fractionIdList.add(fraction_id);
		  		
 		  	}  		
		  		
		  		//get fractions 
		  		PreparedStatement selectFractsStat = null;	
		  		String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ? AND `prot_accession` =? ";
	  			
		  		for(int fractId:fractionIdList)
		  		{

			  		FractionBean fb = new FractionBean();
			  		fb.setMinRange(minRange);
			  		fb.setMaxRange(maxRange);
			  		fb.setFractionId(fractId);
			  		 if(conn == null || conn.isClosed()){
							Class.forName(driver).newInstance();
							conn = DriverManager.getConnection(url+dbName, userName, password);
							}
					   selectFractsStat = conn.prepareStatement(selectFract); 
			    	selectFractsStat.setInt(1,fractId);
			    	selectFractsStat.setString(2,accession);
		  		   	rs = selectFractsStat.executeQuery();
		  		   	Map<String,ProteinBean> proteinList = new HashMap<String, ProteinBean>();
		  		   	
		  		   	while(rs.next())
			  		{
		  		   		ProteinBean pb = new ProteinBean();//fraction_id		  			
			  			pb.setAccession(rs.getString("prot_accession"));
			  			pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
			  			pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
			  			pb.setAveragePrecursorIntensityPerFraction(rs.getDouble("average_ precursor_intensity"));
			  			proteinList.put(pb.getAccession(),pb);			  			
			  		}  

		  		   	fb.setProteinList(proteinList);
		  		   	fractionsList.put(fb.getFractionId(),fb);
		  		   	
		  		}
		  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return fractionsList;
		
		
	}

	public List<ProteinBean> searchProteinByName(String protSearch, int expId) {
		PreparedStatement selectProStat = null;
		List<ProteinBean> proteinsList = new ArrayList<ProteinBean>();
	    String selectPro = "SELECT `accession` FROM `proteins_table` WHERE `description` LIKE (?)";
		List<String> accessionList = new ArrayList<String>();
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectProStat = conn.prepareStatement(selectPro); 
			selectProStat.setString(1,"%"+protSearch+"%");
	  		ResultSet rs = selectProStat.executeQuery();
	  		while(rs.next())
	  		{
	  			accessionList.add(rs.getString("accession"));
	  		}
	  		
	  		for(String accession : accessionList)
	  		{
	  			ProteinBean pb = this.getProtein(accession, expId);
	  			if(pb != null)
	  				proteinsList.add(pb);
	  		}
	  		//conn.close();
	  		return proteinsList;
	    }catch(Exception e){e.printStackTrace();}
	    
		return null;
	}

	public List<ProteinBean> searchProteinByPeptideSequence(String protSearch,	int expId) {
		PreparedStatement selectProStat = null;
		List<ProteinBean> proteinsList = new ArrayList<ProteinBean>();
	    
		
		
		String selectPro = "SELECT `protein` , `peptide_id`  FROM `proteins_peptides_table` WHERE `sequence` = ? ;";
		Map<Integer,String> accessionList = new HashMap<Integer,String>();
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectProStat = conn.prepareStatement(selectPro); 
			selectProStat.setString(1,protSearch);
	  		ResultSet rs = selectProStat.executeQuery();
	  		while(rs.next())
	  		{
	  			accessionList.put(rs.getInt("peptide_id"),rs.getString("protein"));
	  		}
	  		
	  		for(int key : accessionList.keySet())
	  		{
	  			boolean test = checkPeptideExp(key,expId);
	  			ProteinBean pb = null;
				if(test)
	  				pb = this.getProtein(accessionList.get(key), expId);
	  			if(pb != null)
	  				proteinsList.add(pb);
	  		}
	  		//conn.close();
	  		return proteinsList;
	    }catch(Exception e){e.printStackTrace();}
	    
		return null;
	}

	private boolean checkPeptideExp(int key, int expId) {
		PreparedStatement selectPepExpStat = null;
		boolean test = false;
		String selectPepExp = "SELECT * FROM `experiment_peptides_table`  WHERE `exp_id` = ? AND `pep_id` = ? ;";
		try {		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   selectPepExpStat = conn.prepareStatement(selectPepExp); 
			selectPepExpStat.setInt(1,expId);
			selectPepExpStat.setInt(2,key);
	  		ResultSet rs = selectPepExpStat.executeQuery();
	  		while(rs.next())
	  		{
	  			test = true;
	  		}
	  		
	  		//conn.close();
	    }catch(Exception e){e.printStackTrace();}
	    
		return test;
	}
	
	
	
	//Security Handling 

	public boolean regUser( String username, String password2,boolean admin) {
		PreparedStatement regUserStat = null;
		boolean test = false;
		String insertUser = "INSERT INTO  `"+dbName+"`.`users_table`(`user_name`,`password`,`admin`) VALUES (?,?,?);";
		 try {		    	
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   regUserStat = conn.prepareStatement(insertUser); 
			regUserStat.setString(1,username.toUpperCase());
			regUserStat.setString(2,password2);
			regUserStat.setString(3,""+admin);
	  		int rs = regUserStat.executeUpdate();
	  		if(rs > 0)
	  		{
	  			test = true;
	  		}
	  		
	  		//conn.close();
	    }catch(Exception e){}
	    
		return test;
	}

	public boolean validateUsername(String username) {
		try
		{		    	
	    	
	  		//get username
			PreparedStatement selectUserStat = null;				
		    String selectuser = "SELECT * FROM `users_table` WHERE `user_name` = ?";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		   selectUserStat = conn.prepareStatement(selectuser); 
		    selectUserStat.setString(1,username.toUpperCase());
	  		ResultSet rs = selectUserStat.executeQuery();
	  		while(rs.next())
 		  	{
 		  		return false;//valid username
		  		
 		  	}  		
	  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return true;//not valid
	}

	public String authenticate( String username) {
		//get password 
  		PreparedStatement selectUserStat = null;
  		try{
	    String selectuser = "SELECT `password` FROM `users_table` WHERE `user_name` =  ?";
	    if(conn == null || conn.isClosed()){
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName, userName, password);
			}
	   selectUserStat = conn.prepareStatement(selectuser); 
	    selectUserStat.setString(1,username.toUpperCase());
  		ResultSet rs = selectUserStat.executeQuery();
  		while(rs.next())
		  	{
		  		return rs.getString("password");//valid username
	  		
		  	}  		
  		//conn.close();
	}catch(Exception e){e.printStackTrace();}
	return null;//not valid
	}

	public User getUser(String username2) {
		PreparedStatement selectUserStat = null;
		
  		try{
	    String selectuser = "SELECT * FROM `users_table` WHERE `user_name` =  ?";
	    if(conn == null || conn.isClosed()){
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName, userName, password);
			}
	   selectUserStat = conn.prepareStatement(selectuser); 
	    selectUserStat.setString(1,username2.toUpperCase());
  		ResultSet rs = selectUserStat.executeQuery();
  		while(rs.next())
		  	{
  				User user = new User();
  				user.setAdmin(Boolean.valueOf(rs.getString("admin")));
  				user.setUsername(username2);
  				return user;
	  		
		  	}  
  		//conn.close();
	}catch(Exception e){e.printStackTrace();}
	return null;//not valid
	}

	public Map<Integer, String> getUsersList() {
		Map<Integer, String> usersList = new HashMap<Integer, String>();
		PreparedStatement selectUsersStat = null;
		
  		try{
	    String selectusers = "SELECT * FROM `users_table` WHERE `admin` = 'false'; ";
	    if(conn == null || conn.isClosed()){
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName, userName, password);
			}
	    selectUsersStat = conn.prepareStatement(selectusers); 
  		ResultSet rs = selectUsersStat.executeQuery();
  		while(rs.next())
		  	{
  			
  				usersList.put(rs.getInt("id"),rs.getString("user_name"));
  				
	  		
		  	}  
  		//conn.close();
	}catch(Exception e){e.printStackTrace();}
	return usersList;//not valid
	}

	public boolean removeUser(String user) {
		try
		{		    	
	    	
	  		
			PreparedStatement removeUserStat = null;				
		    String removeuser = "DELETE  FROM `users_table` WHERE `user_name` = ?;";
		    if(conn == null || conn.isClosed()){
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url+dbName, userName, password);
				}
		   removeUserStat = conn.prepareStatement(removeuser); 
		    removeUserStat.setString(1,user.toUpperCase());
	  		int rs = removeUserStat.executeUpdate();
	  		if(rs > 0)
 		  	{
 		  		return true;//valid username
		  		
 		  	}  		
	  		//conn.close();
		}catch(Exception e){e.printStackTrace();}
		return false;//not valid
	}

	public boolean updateUserPassword(String username2, String newpassword) {

		try{
			 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			   
			String updateProtDesc = "UPDATE  `"+dbName+"`.`users_table` SET `password`= ? WHERE `user_name` = ? ;";
			PreparedStatement	updateProtDescStat =  conn.prepareStatement(updateProtDesc); 
			updateProtDescStat.setString(1,newpassword);
			updateProtDescStat.setString(2, username2.toUpperCase());
			int test = updateProtDescStat.executeUpdate();
			if(test  > 0)
				return true;
			//conn.close();
		}catch(Exception e2){e2.printStackTrace();}
		
		return false;
	}

	///new v-2

	public List<ProteinBean> searchOtherProteins(String accession,	int expId, List<ProteinBean> protList) {
		PreparedStatement selectProStat = null;
	    String selectPro = "SELECT `prot_accession` FROM `experiment_protein_table` Where `exp_id`=? AND `other_protein(s)` LIKE (?);";
		
	    try {		    	
	    	 if(conn == null || conn.isClosed()){
					Class.forName(driver).newInstance();
					conn = DriverManager.getConnection(url+dbName, userName, password);
					}
			selectProStat = conn.prepareStatement(selectPro); 
			selectProStat.setString(2,"%"+accession+"%");
			selectProStat.setInt(1, expId);
	  		ResultSet rs = selectProStat.executeQuery();
	  		List<String> accsList = new ArrayList<String>();
	  		while(rs.next())
	  		{
	  			accsList.add(rs.getString("prot_accession"));
	  			
	  		}
	  		for(String acc:accsList)
	  		{
	  			ProteinBean pb = this.getProtein(acc,expId);
	  			protList.add(pb);
	  			
	  		}
	  		return protList;
	    }catch(Exception e){e.printStackTrace();return null;}
	}

}
