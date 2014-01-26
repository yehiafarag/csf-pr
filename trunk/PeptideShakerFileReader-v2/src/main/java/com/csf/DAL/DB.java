package com.csf.DAL;

import com.pepshack.util.beans.ExperimentBean;
import com.pepshack.util.beans.FractionBean;
import com.pepshack.util.beans.PeptideBean;
import com.pepshack.util.beans.ProteinBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yehia Farag
 */
public class DB implements Serializable {

    private Connection conn_ii = null;
    private Connection conn = null;
    private Connection conn_i = null;
    private final String url;
    private final String dbName;
    private final String driver;
    private final String userName;
    private final String password;

    public DB(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    public synchronized boolean createTables() throws SQLException//create CSF the database tables if not exist
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
            } catch (SQLException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (InstantiationException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (conn_ii == null || conn_ii.isClosed()) {
                Class.forName(driver).newInstance();
                conn_ii = DriverManager.getConnection(url + dbName, userName, password);
            }
            try {
                Statement st = conn_ii.createStatement();
                //CREATE TABLE  `users_table`
                String users_table = "CREATE TABLE IF NOT EXISTS `users_table` (  `id` int(20) NOT NULL auto_increment,  `password` varchar(100) NOT NULL,  `admin` varchar(5) NOT NULL default 'FALSE',  `user_name` varchar(20) NOT NULL,  `email` varchar(100) NOT NULL,  PRIMARY KEY  (`email`),  KEY `id` (`id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(users_table);

                //CREATE TABLE `experiments_table`
                String experiments_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (\n"
                        + "  `exp_id` int(11) NOT NULL auto_increment,\n"
                        + "  `fraction_range` int(2) NOT NULL default '0',\n"
                        + "  `name` varchar(100) NOT NULL,\n"
                        + "  `fractions_number` int(11) NOT NULL default '0',\n"
                        + "  `ready` int(11) NOT NULL default '0',\n"
                        + "  `uploaded_by` varchar(100) NOT NULL,\n"
                        + "  `peptide_file` int(2) NOT NULL default '0',\n"
                        + "  `species` varchar(100) NOT NULL,\n"
                        + "  `sample_type` varchar(100) NOT NULL,\n"
                        + "  `sample_processing` varchar(100) NOT NULL,\n"
                        + "  `instrument_type` varchar(100) NOT NULL,\n"
                        + "  `frag_mode` varchar(100) NOT NULL,\n"
                        + "  `proteins_number` int(11) NOT NULL default '0',\n"
                        + "  `peptides_number` int(11) NOT NULL default '0',\n"
                        + "  `email` varchar(100) NOT NULL,\n"
                        + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',\n"
                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',\n"
                        + "  `exp_type` int(10) NOT NULL default '0',\n"
                        + "  `valid_prot` int(11) NOT NULL default '0',\n"
                        + "  PRIMARY KEY  (`exp_id`)\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=0 ;";
                st.executeUpdate(experiments_table);


                //CREATE TABLE experiment_protein_table
                String experiment_protein_table = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (\n"
                        + "  `exp_id` int(11) NOT NULL,\n"
                        + "  `prot_accession` varchar(30) NOT NULL,\n"
                        + "  `other_protein(s)` varchar(1000) default NULL,\n"
                        + "  `protein_inference_class` varchar(100) default NULL,\n"
                        + "  `sequence_coverage(%)` double default NULL,\n"
                        + "  `observable_coverage(%)` double default NULL,\n"
                        + "  `confident_ptm_sites` varchar(500) default NULL,\n"
                        + "  `number_confident` varchar(500) default NULL,\n"
                        + "  `other_ptm_sites` varchar(500) default NULL,\n"
                        + "  `number_other` varchar(500) default NULL,\n"
                        + "  `number_validated_peptides` int(11) default NULL,\n"
                        + "  `number_validated_spectra` int(11) default NULL,\n"
                        + "  `em_pai` double default NULL,\n"
                        + "  `nsaf` double default NULL,\n"
                        + "  `mw_(kDa)` double default NULL,\n"
                        + "  `score` double default NULL,\n"
                        + "  `confidence` double default NULL,\n"
                        + "  `starred` varchar(5) default NULL,\n"
                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                        + "  `non_enzymatic_peptides` varchar(5) NOT NULL,\n"
                        + "  `gene_name` varchar(50) NOT NULL default 'Not Available',\n"
                        + "  `chromosome_number` varchar(20) NOT NULL default '',\n"
                        + "  `prot_key` varchar(500) NOT NULL,\n"
                        + "  `valid` varchar(7) NOT NULL default 'false',\n"
                        + "  `description` varchar(500) NOT NULL,\n"
                        + "  `prot_group_id` int(255) NOT NULL auto_increment,\n"
                        + "  PRIMARY KEY  (`prot_group_id`),\n"
                        + "  KEY `exp_id` (`exp_id`),\n"
                        + "  KEY `prot_key` (`prot_key`(333))\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=0 ;";
                st.executeUpdate(experiment_protein_table);

                //CREATE TABLE experiment_fractions_table
                String experiment_fractions_table = "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` (  `exp_id` int(11) NOT NULL,`fraction_id` int(11) NOT NULL auto_increment,  `min_range` double NOT NULL default '0',"
                        + "  `max_range` double NOT NULL default '0', `index` int(11) NOT NULL default '0',  PRIMARY KEY  (`fraction_id`),  KEY `exp_id` (`exp_id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(experiment_fractions_table);


                //  CREATE TABLE  `experiment_peptides_table`
                String experiment_peptide_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (  `exp_id` INT NOT NULL DEFAULT  '0',  `pep_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE  ) ENGINE = MYISAM ;";
                st.executeUpdate(experiment_peptide_table);

                // CREATE TABLE  `proteins_peptides_table`
                String proteins_peptides_table = "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` (\n"
                        + "  `protein` varchar(70) default NULL,\n"
                        + "  `other_protein(s)` text,\n"
                        + "  `peptide_protein(s)` text,\n"
                        + "  `other_protein_description(s)` text,\n"
                        + "  `peptide_proteins_description(s)` text,\n"
                        + "  `aa_before` varchar(2000) default NULL,\n"
                        + "  `sequence` varchar(300) default NULL,\n"
                        + "  `aa_after` varchar(2000) default NULL,\n"
                        + "  `peptide_start` text,\n"
                        + "  `peptide_end` text,\n"
                        + "  `variable_modification` varchar(500) default NULL,\n"
                        + "  `location_confidence` varchar(500) default NULL,\n"
                        + "  `precursor_charge(s)` varchar(70) default NULL,\n"
                        + "  `number_of_validated_spectra` int(20) default NULL,\n"
                        + "  `score` double NOT NULL default '0',\n"
                        + "  `confidence` double NOT NULL default '0',\n"
                        + "  `peptide_id` int(50) NOT NULL default '0',\n"
                        + "  `fixed_modification` varchar(100) default NULL,\n"
                        + "  `protein_inference` varchar(500) default NULL,\n"
                        + "  `sequence_tagged` varchar(500) default NULL,\n"
                        + "  `enzymatic` varchar(5) default NULL,\n"
                        + "  `validated` double default NULL,\n"
                        + "  `starred` varchar(5) default NULL,\n"
                        + "  `glycopattern_position(s)` varchar(100) default NULL,\n"
                        + "  `deamidation_and_glycopattern` varchar(5) default NULL,\n"
                        + "  `exp_id` int(250) NOT NULL default '0',\n"
                        + "  `likelyNotGlycosite` varchar(5) NOT NULL default 'FALSE',\n"
                        + "  KEY `peptide_id` (`peptide_id`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(proteins_peptides_table);
                
                
//                //TEMPRARLY ADD WILL REMOVE NEXT TIME 
//                String alterPeptideTable = "ALTER TABLE  `proteins_peptides_table` ADD  `likelyNotGlycosite` VARCHAR( 5 ) NOT NULL DEFAULT  'FALSE';";
//                st.executeUpdate(alterPeptideTable);
                //CREATE TABLE fractions_table
                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (  `fraction_id` int(11) NOT NULL,`prot_accession` varchar(500) NOT NULL,"
                        + "`number_peptides` int(11) NOT NULL default '0',  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `number_spectra` int(11) NOT NULL default '0',`average_ precursor_intensity` double default NULL," + "KEY `prot_accession` (`prot_accession`), KEY `fraction_id` (`fraction_id`),	FOREIGN KEY (`prot_accession`) REFERENCES proteins_table(`accession`) ON DELETE CASCADE,"
                        + "FOREIGN KEY (`fraction_id`) REFERENCES experiment_fractions_table(`fraction_id`) ON DELETE CASCADE	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(fractions_table);

                //CREATE TABLE experiment_peptides_proteins_table
                String experiment_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (\n"
                        + "  `exp_id` varchar(50) NOT NULL,\n"
                        + "  `peptide_id` int(50) NOT NULL,\n"
                        + "  `protein` varchar(1000) NOT NULL\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(experiment_peptides_proteins_table);

                //CREATE TABLEstandard_plot_proteins
                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(standard_plot_proteins);

                conn_ii.close();

            } catch (SQLException s) {
                throw s;
            }
        } catch (SQLException e) {
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public int setupExperiment(ExperimentBean exp) {
        int id = 0;
        String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready` ,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`proteins_number` ,`email` ,`pblication_link`,`description`,`fraction_range`,`peptide_file`,`fractions_number`,`peptides_number`,`exp_type`,`valid_prot`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            PreparedStatement insertExpStat = conn.prepareStatement(insertExp, Statement.RETURN_GENERATED_KEYS);
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
            insertExpStat.setInt(14, exp.getPeptidesInclude());
            insertExpStat.setInt(15, exp.getFractionsNumber());
            insertExpStat.setInt(16, exp.getPeptidesNumber());
            insertExpStat.setInt(17, exp.getExpType());
            insertExpStat.setInt(18, exp.getNumberValidProt());
            insertExpStat.executeUpdate();
            ResultSet rs = insertExpStat.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            insertExpStat.clearParameters();
            insertExpStat.close();
            rs.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return id;
    }

    public synchronized int insertProteinExper(int expId, ProteinBean pb, String key) {
        
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtExp = "INSERT INTO  `" + dbName + "`.`experiment_protein_table` (`exp_id` ,`prot_accession` ,`other_protein(s)` ,`protein_inference_class` ,`sequence_coverage(%)` ,`observable_coverage(%)` ,`confident_ptm_sites` ,`number_confident` ,`other_ptm_sites` ,`number_other` ,`number_validated_peptides` ,`number_validated_spectra` ,`em_pai` ,`nsaf` ,`mw_(kDa)` ,`score` ,`confidence` ,`starred`,`non_enzymatic_peptides`,`gene_name`,`chromosome_number`,`prot_key`,`valid`,`description`)VALUES (?,?,?,?,?,  ?, ?, ?, ?,  ?,  ?, ?, ?, ?, ?,  ?, ?,?,?,?,?,?,?,?);";
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

            if (pb.getGeneName() == null) {
                insertProtStat.setString(20, "");
            } else {
                insertProtStat.setString(20, pb.getGeneName().toUpperCase());
            }
            if (pb.getChromosomeNumber() == null) {
                insertProtStat.setString(21, "");
            } else {
                insertProtStat.setString(21, pb.getChromosomeNumber().toUpperCase());
            }
            insertProtStat.setString(22, (String.valueOf(key).toUpperCase()));
            insertProtStat.setString(23, (pb.isValidated() + "").toUpperCase());
            insertProtStat.setString(24, pb.getDescription().toUpperCase());

            int test = insertProtStat.executeUpdate();
            insertProtStat.close();
            return test;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        }
         catch (InstantiationException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        }
         catch (IllegalAccessException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        }
    }

    public synchronized boolean updatePeptideFile(ExperimentBean exp) {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int counter = 0;
            for (PeptideBean pepb : exp.getPeptideList().values()) {
                if (pepb.getDecoy() == 1) {
                    continue;
                }
                insertPeptide(-1, pepb, exp.getExpId(),conn);
                counter++;
                if (counter == 500) {
                    conn.close();     
                    System.gc();               
                    Thread.sleep(100);
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                    counter = 0;
                   
                }
            }


        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public synchronized int insertPeptide(int pepId, PeptideBean pepb, int expId,Connection conn) {
        String insertPeptide = "INSERT INTO  `" + dbName + "`.`proteins_peptides_table` (`protein` ,`other_protein(s)` ,`peptide_protein(s)` ,`other_protein_description(s)` ,`peptide_proteins_description(s)` ,`aa_before` ,`sequence` ,"
                + "`aa_after` ,`peptide_start` ,`peptide_end` ,`variable_modification` ,`location_confidence` ,`precursor_charge(s)` ,`number_of_validated_spectra` ,`score` ,`confidence` ,`peptide_id`,`fixed_modification`,`protein_inference`,`sequence_tagged`,`enzymatic`,`validated`,`starred`,`glycopattern_position(s)`,`deamidation_and_glycopattern`,`likelyNotGlycosite`,`exp_id` )VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
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
                insertPeptExpStat.clearParameters();
                insertPeptExpStat.close();

            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int test = -1;
        PreparedStatement insertPeptideStat = null;
        String pepKey = ",";
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
                insertPeptideStat.setString(25, "FALSE");
            }
            if (pepb.isLikelyNotGlycopeptide() != null && pepb.isLikelyNotGlycopeptide()) {
                insertPeptideStat.setString(26, String.valueOf(pepb.isLikelyNotGlycopeptide()).toUpperCase());
            } else {
                insertPeptideStat.setString(26, "FALSE");
            }
            insertPeptideStat.setInt(27, expId);

            test = insertPeptideStat.executeUpdate();

            insertPeptideStat.clearParameters();
            insertPeptideStat.close();
           
            
            if (!pepb.getProtein().equalsIgnoreCase("shared peptide")) {
                pepKey = pepKey + pepb.getProtein().toUpperCase();
            }
            if (pepb.getOtherProteins() != null && !pepb.getOtherProteins().equals("") && !pepKey.endsWith(",")) {
                pepKey = pepKey + "," + pepb.getOtherProteins().toUpperCase();
            } else if (pepb.getOtherProteins() != null && !pepb.getOtherProteins().equals("") && pepKey.endsWith(",")) {
                pepKey = pepKey + pepb.getOtherProteins().toUpperCase();
            }
            if (pepb.getPeptideProteins() != null && !pepb.getPeptideProteins().equals("") && !pepKey.endsWith(",")) {
                pepKey = pepKey + "," + pepb.getPeptideProteins().toUpperCase();
            } else if (pepb.getPeptideProteins() != null && !pepb.getPeptideProteins().equals("") && pepKey.endsWith(",")) {
                pepKey = pepKey + pepb.getPeptideProteins().toUpperCase();
            }
            if (!pepKey.endsWith(",")) {
                pepKey = pepKey + ",";
            }
           
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            try {
                insertPeptideStat.close();
            } catch (SQLException e1) {
                System.out.println(e1.getLocalizedMessage());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        insertExpProtPept(expId, pepId, pepKey,conn);

        return test;
    }

    public int insertExpProtPept(int expId, int pepId, String pepKey,Connection conn) {
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
            insertExpProtPeptQStat.setString(3, pepKey.toUpperCase());
            test = insertExpProtPeptQStat.executeUpdate();
            insertExpProtPeptQStat.close();

//            conn.close();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
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
            PreparedStatement insertFractExpStat ;//conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
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
        } catch (SQLException exc) {
            System.out.println(exc.getLocalizedMessage());
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
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
            insertProtFracStat.setString(2,fpb.getAccession().toUpperCase()+","+fpb.getOtherProteins().toUpperCase());
            insertProtFracStat.setInt(3, fpb.getNumberOfPeptidePerFraction());
            insertProtFracStat.setInt(4, fpb.getNumberOfSpectraPerFraction());
            insertProtFracStat.setDouble(5, fpb.getAveragePrecursorIntensityPerFraction());
            test = insertProtFracStat.executeUpdate();
            insertProtFracStat.close();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        }
        return test;

    }

    public boolean checkName(String name) throws SQLException {
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
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            throw new SQLException();
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return true;
    }
}
