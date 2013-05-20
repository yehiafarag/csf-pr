/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csf.DAL;

import com.pepshack.util.beans.ExperimentBean;
import com.pepshack.util.beans.FractionBean;
import com.pepshack.util.beans.PeptideBean;
import com.pepshack.util.beans.ProteinBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Yehia Mokhtar
 */

public class DB {
    private Connection conn = null;
    private Connection conn_i = null;
    private String url;
    private String dbName;
    private String driver;
    private String userName;
    private String password;
    private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;
    
    public DB(String url, String dbName, String driver, String userName, String password) {

        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;

        try {
            //Class.forName(driver).newInstance();
        } catch (Exception e) {
        };

    }
    public synchronized boolean createTables()//create CSF the database tables if not exist
    {


        try {
            try {
                if (conn_i == null || conn_i.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn_i = DriverManager.getConnection(url + "mysql", userName, password);
                }
                Statement statement = conn_i.createStatement();
                String csfSQL = "CREATE DATABASE IF NOT exists  " + dbName;
                statement.executeUpdate(csfSQL);
                conn_i.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            try {
                Statement st = conn.createStatement();
                //CREATE TABLE  `users_table`
                String users_table = "CREATE TABLE IF NOT EXISTS `users_table` (  `id` int(20) NOT NULL auto_increment,  `password` varchar(100) NOT NULL,  `admin` varchar(5) NOT NULL default 'FALSE',  `user_name` varchar(20) NOT NULL,  `email` varchar(100) NOT NULL,  PRIMARY KEY  (`email`),  KEY `id` (`id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(users_table);

                //CREATE TABLE `experiments_table`
                String experiments_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (  `exp_id` int(11) NOT NULL auto_increment, `fraction_range` int(2) NOT NULL default '0',  `name` varchar(100) NOT NULL,  `fractions_number` int(11) NOT NULL default '0',  `ready` int(11) NOT NULL default '0',  `uploaded_by` varchar(100) NOT NULL,  `peptide_file` int(2) NOT NULL default '0',"
                        + "	 `species` varchar(100) NOT NULL,  `sample_type` varchar(100) NOT NULL,  `sample_processing` varchar(100) NOT NULL,  `instrument_type` varchar(100) NOT NULL,  `frag_mode` varchar(100) NOT NULL,  `proteins_number` int(11) NOT NULL default '0',  `peptides_number` int(11) NOT NULL default '0',  `email` varchar(100) NOT NULL,  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',"
                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',  PRIMARY KEY  (`exp_id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";

                st.executeUpdate(experiments_table);

                //CREATE TABLE proteins_table
                String proteins_table = "CREATE TABLE IF NOT EXISTS `proteins_table` ("
                        + "`id` int(11) NOT NULL auto_increment,  `accession` varchar(30) NOT NULL,"
                        + "`description` varchar(500) NOT NULL default 'No Description Available',"
                        + "PRIMARY KEY  (`accession`),  KEY `id` (`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(proteins_table);

                //CREATE TABLE experiment_protein_table
                String experiment_protein_table = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (  `exp_id` int(11) NOT NULL,  `prot_accession` varchar(30) NOT NULL,  `other_protein(s)` varchar(1000) default NULL,  `protein_inference_class` varchar(100) default NULL,  `sequence_coverage(%)` double default NULL,  `observable_coverage(%)` double default NULL,  `confident_ptm_sites` varchar(500) default NULL,  `number_confident` varchar(500) default NULL,  `other_ptm_sites` varchar(500) default NULL,  `number_other` varchar(500) default NULL,  `number_validated_peptides` int(11) default NULL,"
                        + "  `number_validated_spectra` int(11) default NULL,  `em_pai` double default NULL,  `nsaf` double default NULL,  `mw_(kDa)` double default NULL,  `score` double default NULL,  `confidence` double default NULL,  `starred` varchar(5) default NULL,   `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,"
                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL, `non_enzymatic_peptides` varchar(5) NOT NULL,  KEY `exp_id` (`exp_id`),  KEY `prot_accession` (`prot_accession`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(experiment_protein_table);

                 //CREATE TABLE experiment_fractions_table
                String experiment_fractions_table = "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` (  `exp_id` int(11) NOT NULL,`fraction_id` int(11) NOT NULL auto_increment,  `min_range` double NOT NULL default '0',"
                        + "  `max_range` double NOT NULL default '0', `index` int(11) NOT NULL default '0',  PRIMARY KEY  (`fraction_id`),  KEY `exp_id` (`exp_id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(experiment_fractions_table);


                //  CREATE TABLE  `experiment_peptides_table`
                String experiment_peptide_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (  `exp_id` INT NOT NULL DEFAULT  '0',  `pep_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE  ) ENGINE = MYISAM ;";
                st.executeUpdate(experiment_peptide_table);

                // CREATE TABLE  `proteins_peptides_table`
                String proteins_peptides_table = "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` (  `protein` varchar(70) default NULL,  `other_protein(s)` text,  `peptide_protein(s)` text,  `other_protein_description(s)` text,  `peptide_proteins_description(s)` text,  `aa_before` varchar(2000) default NULL,  `sequence` varchar(300) default NULL,  `aa_after` varchar(2000) default NULL,  `peptide_start` text,  `peptide_end` text,"
                        + "  `variable_modification` varchar(500) default NULL,  `location_confidence` varchar(500) default NULL,  `precursor_charge(s)` varchar(70) default NULL,  `number_of_validated_spectra` int(20) default NULL,  `score` double NOT NULL default '0',  `confidence` double NOT NULL default '0',  `peptide_id` int(50) NOT NULL default '0',  `fixed_modification` varchar(100) default NULL,  `protein_inference` varchar(500) default NULL,  `sequence_tagged` varchar(500) default NULL,  `enzymatic` varchar(5) default NULL,"
                        + "  `validated` double default NULL,  `starred` varchar(5) default NULL,`glycopattern_position(s)` varchar(100) default NULL, `deamidation_and_glycopattern` varchar(5) default NULL,  KEY `peptide_id` (`peptide_id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(proteins_peptides_table);

                //CREATE TABLE fractions_table
                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (  `fraction_id` int(11) NOT NULL,`prot_accession` varchar(30) NOT NULL,"
                        + "`number_peptides` int(11) NOT NULL default '0',  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `number_spectra` int(11) NOT NULL default '0',`average_ precursor_intensity` double default NULL," + "KEY `prot_accession` (`prot_accession`), KEY `fraction_id` (`fraction_id`),	FOREIGN KEY (`prot_accession`) REFERENCES proteins_table(`accession`) ON DELETE CASCADE,"
                        + "FOREIGN KEY (`fraction_id`) REFERENCES experiment_fractions_table(`fraction_id`) ON DELETE CASCADE	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(fractions_table);

                //CREATE TABLE experiment_peptides_proteins_table
                String experiment_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (  `exp_id` varchar(50) NOT NULL,  `peptide_id` int(50) NOT NULL,  `protein` varchar(70) NOT NULL,  UNIQUE KEY `exp_id` (`exp_id`,`peptide_id`,`protein`),  KEY `peptide_id` (`peptide_id`),  KEY `protein` (`protein`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(experiment_peptides_proteins_table);
                
                 //CREATE TABLEstandard_plot_proteins
                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(standard_plot_proteins);






            } catch (SQLException s) {
                s.printStackTrace();
                return false;
            }
            // 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }
    
    public int setupExperiment(ExperimentBean exp)
    {
        PreparedStatement insertExpStat = null;
            int id = 0;
            int test = 0;

            String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready` ,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`proteins_number` ,	`email` ,`pblication_link`,`description`,`fraction_range`,`peptide_file`,`fractions_number`,`peptides_number`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                insertExpStat = conn.prepareStatement(insertExp, Statement.RETURN_GENERATED_KEYS);
               
                insertExpStat.setString(1, exp.getName().toUpperCase());
                insertExpStat.setInt(2, 2);
                insertExpStat.setString(3, exp.getUploadedByName().toUpperCase());
                insertExpStat.setString(4, exp.getSpecies());
                insertExpStat.setString(5, exp.getSampleType());
                insertExpStat.setString(6, exp.getSampleProcessing());
                insertExpStat.setString(7, exp.getInstrumentType());
                insertExpStat.setString(8, exp.getFragMode());
                insertExpStat.setInt(9, exp.getProteinsNumber());
                insertExpStat.setString(10, exp.getEmail().toUpperCase());
                if (exp.getPublicationLink() != null) {
                    insertExpStat.setString(11, exp.getPublicationLink());
                } else {
                    insertExpStat.setString(11, "NOT AVAILABLE");
                }
                insertExpStat.setString(12, exp.getDescription());
                insertExpStat.setInt(13, 1);
                insertExpStat.setInt(14,exp.getPeptidesInclude());
                insertExpStat.setInt(15,exp.getFractionsNumber());
                insertExpStat.setInt(16,exp.getPeptidesNumber());
                insertExpStat.executeUpdate();
                ResultSet rs = insertExpStat.getGeneratedKeys();
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                insertExpStat.clearParameters();
                insertExpStat.close();
                rs.close();
            }catch(Exception e){e.printStackTrace();}
                return id;   
    }
    public synchronized int insertProt( String accession, String desc)//fill protein table
    {
        int test = -1;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProt = "INSERT INTO  `" + dbName + "`.`proteins_table` (`accession` ,`description`)VALUES (?,?);";
            PreparedStatement insertProtStat = conn.prepareStatement(insertProt, Statement.RETURN_GENERATED_KEYS);
            insertProtStat.setString(1, accession.toUpperCase());
            insertProtStat.setString(2, desc.toUpperCase());
            test = insertProtStat.executeUpdate();
            insertProtStat.close();
        } catch (Exception e) {
            test = updateProt(accession, desc);
        }
        return test;
    }
    private synchronized int updateProt(String accession, String desc)//fill protein table
    {
        int test = -1;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProt = "UPDATE  `" + dbName + "`.`proteins_table` SET `description` = ? WHERE `accession`=?;";
            PreparedStatement insertProtStat = conn.prepareStatement(insertProt, Statement.RETURN_GENERATED_KEYS);
            insertProtStat.setString(1, desc.toUpperCase());
            insertProtStat.setString(2, accession.toUpperCase());
            test = insertProtStat.executeUpdate();
            insertProtStat.clearParameters();;
            insertProtStat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return test;
    }
    
 public synchronized int insertProteinExper(int expId, ProteinBean pb) {
        int test = -1;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtExp = "INSERT INTO  `" + dbName + "`.`experiment_protein_table` (`exp_id` ,`prot_accession` ,`other_protein(s)` ,`protein_inference_class` ,`sequence_coverage(%)` ,`observable_coverage(%)` ,`confident_ptm_sites` ,`number_confident` ,`other_ptm_sites` ,`number_other` ,`number_validated_peptides` ,`number_validated_spectra` ,`em_pai` ,`nsaf` ,`mw_(kDa)` ,`score` ,`confidence` ,`starred`,`non_enzymatic_peptides`)VALUES (?,?,?,  ?, ?, ?, ?,  ?,  ?, ?, ?, ?, ?,  ?, ?,?,?,?,?);";
            PreparedStatement insertProtStat = conn.prepareStatement(insertProtExp, Statement.RETURN_GENERATED_KEYS);
            insertProtStat.setInt(1, expId);
            insertProtStat.setString(2, pb.getAccession().toUpperCase());
            insertProtStat.setString(3, pb.getOtherProteins().toUpperCase());
            insertProtStat.setString(4, pb.getProteinInferenceClass().toUpperCase());
            insertProtStat.setDouble(5, pb.getSequenceCoverage());
            insertProtStat.setDouble(6, pb.getObservableCoverage());
            insertProtStat.setString(7, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
            insertProtStat.setString(8, pb.getNumberConfident().toString());
            insertProtStat.setString(9, pb.getOtherPtmSites().toUpperCase());
            insertProtStat.setString(10, pb.getNumberOfOther().toUpperCase());
            insertProtStat.setInt(11, pb.getNumberValidatedPeptides());
            insertProtStat.setInt(12, pb.getNumberValidatedSpectra());
            insertProtStat.setDouble(13, pb.getEmPai());
            insertProtStat.setDouble(14, pb.getNsaf());
            insertProtStat.setDouble(15, pb.getMw_kDa());
            insertProtStat.setDouble(16, pb.getScore());
            insertProtStat.setDouble(17, pb.getConfidence());
            insertProtStat.setString(18, String.valueOf(pb.isStarred()));
            insertProtStat.setString(19, (String.valueOf(pb.isNonEnzymaticPeptides()).toUpperCase()));
            test = insertProtStat.executeUpdate();
            insertProtStat.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return test;
    }
 
 
 public synchronized boolean updatePeptideFile(ExperimentBean exp) {
       
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
           

               
                int counter = 0;
                for (PeptideBean pepb : exp.getPeptideList().values()) {
                    insertPeptide(-1, pepb, exp.getExpId());
                    counter++;
                    if (counter == 10000) {
                        conn.close();
                        Thread.sleep(100);
                        Class.forName(driver).newInstance();
                        conn = DriverManager.getConnection(url + dbName, userName, password);
                        counter = 0;
                    }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

 
 public synchronized int insertPeptide(int pepId, PeptideBean pepb, int expId) {
        String insertPeptide = "INSERT INTO  `" + dbName + "`.`proteins_peptides_table` (`protein` ,`other_protein(s)` ,`peptide_protein(s)` ,`other_protein_description(s)` ,`peptide_proteins_description(s)` ,`aa_before` ,`sequence` ,"
                + "`aa_after` ,`peptide_start` ,`peptide_end` ,`variable_modification` ,`location_confidence` ,`precursor_charge(s)` ,`number_of_validated_spectra` ,`score` ,`confidence` ,`peptide_id`,`fixed_modification`,`protein_inference`,`sequence_tagged`,`enzymatic`,`validated`,`starred`,`glycopattern_position(s)`,`deamidation_and_glycopattern` )VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?);";
        if (pepId == -1)//generate peptide id
        {
            String insertPeptideExp = "INSERT INTO  `" + dbName + "`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                PreparedStatement insertPeptExpStat = conn.prepareStatement(insertPeptideExp, Statement.RETURN_GENERATED_KEYS);
                insertPeptExpStat.setInt(1, expId);
                insertPeptExpStat.executeUpdate();
                ResultSet rs = insertPeptExpStat.getGeneratedKeys();
                while (rs.next()) {
                    pepId = rs.getInt(1);
                }
                rs.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int test = -1;
        PreparedStatement insertPeptideStat = null;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            insertPeptideStat = conn.prepareStatement(insertPeptide, Statement.RETURN_GENERATED_KEYS);
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
            insertPeptideStat.setString(18, pepb.getFixedModification().toUpperCase());
            insertPeptideStat.setString(19, pepb.getProteinInference());
            insertPeptideStat.setString(20, pepb.getSequenceTagged());
            insertPeptideStat.setString(21, String.valueOf(pepb.isEnzymatic()).toUpperCase());
            insertPeptideStat.setDouble(22, pepb.getValidated());
            insertPeptideStat.setString(23, String.valueOf(pepb.isStarred()).toUpperCase());
            if (pepb.getGlycopatternPositions() != null) {
                insertPeptideStat.setString(24, pepb.getGlycopatternPositions());
            } else {
                insertPeptideStat.setString(24, null);
            }
            if (pepb.isDeamidationAndGlycopattern() != null && pepb.isDeamidationAndGlycopattern()) {
                insertPeptideStat.setString(25, String.valueOf(pepb.isDeamidationAndGlycopattern()).toUpperCase());
            } else {
                insertPeptideStat.setString(25, "");
            }

            test = insertPeptideStat.executeUpdate();

            insertPeptideStat.clearParameters();
            insertPeptideStat.close();

            insertExpProtPept(expId, pepId, pepb.getProtein().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                insertPeptideStat.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return test;
    }
  public int insertExpProtPept(int expId, int pepId, String accession) {
        int test = -1;
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertExpProtPeptQ = "INSERT INTO  `" + dbName + "`.`experiment_peptides_proteins_table` (`exp_id` ,`peptide_id`,`protein`)VALUES (?,?,?);";
            PreparedStatement insertExpProtPeptQStat = conn.prepareStatement(insertExpProtPeptQ);
            insertExpProtPeptQStat.setInt(1, expId);
            insertExpProtPeptQStat.setInt(2, pepId);
            insertExpProtPeptQStat.setString(3, accession.toUpperCase());
            test = insertExpProtPeptQStat.executeUpdate();
            insertExpProtPeptQStat.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return test;
    }
   public synchronized boolean insertFractions(ExperimentBean exp) {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String insertFractExp = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            PreparedStatement insertFractExpStat = conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
            int fractId = 0;

            for (FractionBean fb : exp.getFractionsList().values()) {
                insertFractExpStat = conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
                insertFractExpStat.setInt(1, exp.getExpId());
                insertFractExpStat.setDouble(2, 0);
                insertFractExpStat.setDouble(3, 0);
                insertFractExpStat.setInt(4, fb.getFractionIndex());
                insertFractExpStat.executeUpdate();
                ResultSet rs = insertFractExpStat.getGeneratedKeys();
                while (rs.next()) {
                    fractId = rs.getInt(1);
                }
                rs.close();
                for (ProteinBean pb : fb.getProteinList().values()) {
                    this.insertProteinFract(fractId, pb);

                }
                
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }
   
   private synchronized int insertProteinFract(int fractId, ProteinBean fpb) {
        int test = -1;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtFract = "INSERT INTO  `" + dbName + "`.`fractions_table` (`fraction_id` ,`prot_accession` ,`number_peptides` ,`number_spectra` ,`average_ precursor_intensity`)VALUES (?, ?,  ?,  ?,  ?);";
            PreparedStatement insertProtFracStat = conn.prepareStatement(insertProtFract, Statement.RETURN_GENERATED_KEYS);
            insertProtFracStat.setInt(1, fractId);
            insertProtFracStat.setString(2, fpb.getAccession().toUpperCase());
            insertProtFracStat.setInt(3, fpb.getNumberOfPeptidePerFraction());
            insertProtFracStat.setInt(4, fpb.getNumberOfSpectraPerFraction());
            insertProtFracStat.setDouble(5, fpb.getAveragePrecursorIntensityPerFraction());

            test = insertProtFracStat.executeUpdate();
            insertProtFracStat.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return test;

    }
   
   public boolean checkName(String name)
   {
       try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }            
            String selectName = "SELECT `name` FROM `experiments_table` where `name`=?;";
            PreparedStatement selectNameStat = conn.prepareStatement(selectName);
            selectNameStat.setString(1, name.toUpperCase());
             ResultSet rs = selectNameStat.executeQuery();
            while (rs.next()) {
                return false;
            }

            selectNameStat.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

   
   
   
   }
  
  
 
  



    
}
