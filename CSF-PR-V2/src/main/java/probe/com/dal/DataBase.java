package probe.com.dal;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.beans.User;

public class DataBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Connection conn = null;
    private Connection conn_i = null;
    private String url;
    private String dbName;
    private String driver;
    private String userName;
    private String password;
    private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    //create tables
    public DataBase(String url, String dbName, String driver, String userName, String password) {

        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;

        try {
            //Class.forName(driver).newInstance();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }

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
                System.err.println(e.getLocalizedMessage());
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
                String experiments_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (\n" + "  `exp_id` int(11) NOT NULL auto_increment,\n" + "  `fraction_range` int(2) NOT NULL default '0',\n" + "  `name` varchar(100) NOT NULL,\n" + "  `fractions_number` int(11) NOT NULL default '0',\n" + "  `ready` int(11) NOT NULL default '0',\n" + "  `uploaded_by` varchar(100) NOT NULL,\n" + "  `peptide_file` int(2) NOT NULL default '0',\n"
                        + "  `species` varchar(100) NOT NULL,\n" + "  `sample_type` varchar(100) NOT NULL,\n" + "  `sample_processing` varchar(100) NOT NULL,\n" + "  `instrument_type` varchar(100) NOT NULL,\n" + "  `frag_mode` varchar(100) NOT NULL,\n" + "  `proteins_number` int(11) NOT NULL default '0',\n" + "  `peptides_number` int(11) NOT NULL default '0',\n" + "  `email` varchar(100) NOT NULL,\n" + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',\n"
                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',\n" + "  `exp_type` int(10) NOT NULL default '0',\n" + "  PRIMARY KEY  (`exp_id`)\n" + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;";
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

                //CREATE TABLE fractions_table
                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (  `fraction_id` int(11) NOT NULL,`prot_accession` varchar(500) NOT NULL,"
                        + "`number_peptides` int(11) NOT NULL default '0',  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `number_spectra` int(11) NOT NULL default '0',`average_ precursor_intensity` double default NULL," + "KEY `prot_accession` (`prot_accession`), KEY `fraction_id` (`fraction_id`),	FOREIGN KEY (`prot_accession`) REFERENCES proteins_table(`accession`) ON DELETE CASCADE,"
                        + "FOREIGN KEY (`fraction_id`) REFERENCES experiment_fractions_table(`fraction_id`) ON DELETE CASCADE	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(fractions_table);

                //CREATE TABLE experiment_peptides_proteins_table
                String experiment_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (  `exp_id` varchar(50) NOT NULL,  `peptide_id` int(50) NOT NULL,  `protein` varchar(70) NOT NULL,  UNIQUE KEY `exp_id` (`exp_id`,`peptide_id`,`protein`),  KEY `peptide_id` (`peptide_id`),  KEY `protein` (`protein`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(experiment_peptides_proteins_table);

                //CREATE TABLEstandard_plot_proteins
                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(standard_plot_proteins);
                conn.close();

            } catch (SQLException s) {
                System.err.println(s.getLocalizedMessage());
                conn.close();
                return false;
            }
            // 
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        }

        return true;

    }

//		Storing Data
    public synchronized boolean setProteinFractionFile(ExperimentBean exp) {
        if (exp.getExpId() == -1)//new Experiment
        {
            PreparedStatement insertExpStat = null;
            PreparedStatement insertFractExpStat = null;
            int expId = 0;
            int fractId = 0;
            int test = 0;
            String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready`,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`fractions_number` ,	`email` ,`pblication_link`,`description`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ;";
            String insertFractExp = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
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
                insertExpStat.setInt(9, exp.getFractionsNumber());
                insertExpStat.setString(10, exp.getEmail().toUpperCase());
                if (exp.getPublicationLink() != null) {
                    insertExpStat.setString(11, exp.getPublicationLink().toUpperCase());
                } else {
                    insertExpStat.setString(11, "NOT AVAILABLE");
                }
                insertExpStat.setString(12, exp.getDescription());
                insertExpStat.executeUpdate();
                ResultSet rs = insertExpStat.getGeneratedKeys();
                while (rs.next()) {
                    expId = rs.getInt(1);
                }
                rs.close();
                for (FractionBean fb : exp.getFractionsList().values()) {
                    insertFractExpStat = conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
                    insertFractExpStat.setInt(1, expId);
                    insertFractExpStat.setDouble(2, fb.getMinRange());
                    insertFractExpStat.setDouble(3, fb.getMaxRange());
                    insertFractExpStat.setInt(4, fb.getFractionIndex());
                    insertFractExpStat.executeUpdate();
                    rs = insertFractExpStat.getGeneratedKeys();
                    while (rs.next()) {
                        fractId = rs.getInt(1);
                    }
                    rs.close();
                    for (ProteinBean pb : fb.getProteinList().values()) {
                        test = this.insertProteinFract(conn, fractId, pb);
//                        test = this.insertProt(conn, pb.getAccession(), pb.getDescription());
                    }
                }
                // 			        
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            }
            if (test > 0) {
                return true;
            }
        }

        return false;
    }

    public synchronized int insertFraction(Connection conn2, FractionBean fraction, int expId) {
        String insertFractExp = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
        int fractId = -1;
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement insertFractExpStat = conn2.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
            insertFractExpStat.setInt(1, expId);
            insertFractExpStat.setDouble(2, 0);
            insertFractExpStat.setDouble(3, 0);
            insertFractExpStat.setInt(4, fraction.getFractionIndex());
            insertFractExpStat.executeUpdate();
            ResultSet rs = insertFractExpStat.getGeneratedKeys();
            while (rs.next()) {
                fractId = rs.getInt(1);
            }
            rs.close();
            for (ProteinBean pb : fraction.getProteinList().values()) {
                this.insertProteinFract(conn2, fractId, pb);
//                this.insertProt(conn2, pb.getAccession(), pb.getDescription());
                this.updateProtExpFraction(pb, expId);
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        return 0;
    }

    private synchronized int insertProteinFract(Connection conn2, int fractId, ProteinBean fpb) {
        int test = -1;
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtFract = "INSERT INTO  `" + dbName + "`.`fractions_table` (`fraction_id` ,`prot_accession` ,`number_peptides` ,`number_spectra` ,`average_ precursor_intensity`)VALUES (?, ?,  ?,  ?,  ?);";
            PreparedStatement insertProtFracStat = conn2.prepareStatement(insertProtFract, Statement.RETURN_GENERATED_KEYS);
            insertProtFracStat.setInt(1, fractId);
            insertProtFracStat.setString(2, fpb.getAccession().toUpperCase());
            insertProtFracStat.setInt(3, fpb.getNumberOfPeptidePerFraction());
            insertProtFracStat.setInt(4, fpb.getNumberOfSpectraPerFraction());
            insertProtFracStat.setDouble(5, fpb.getAveragePrecursorIntensityPerFraction());

            test = insertProtFracStat.executeUpdate();
            insertProtFracStat.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return -1;
        }
        return test;
    }

    public synchronized int updateFractionNumber(Connection conn2, int expId, int fractionsNumber) {
        int test = -1;
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            String updateFractionNumber = "UPDATE  `experiments_table` SET `fractions_number` = ? WHERE WHERE `exp_id` = ?;";
            PreparedStatement updateFractStat = conn2.prepareStatement(updateFractionNumber);
            updateFractStat.setInt(1, fractionsNumber);
            updateFractStat.setInt(2, expId);
            test = updateFractStat.executeUpdate();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return test;

    }

    public synchronized boolean updateExperiment(Connection conn2, ExperimentBean exp) {
        PreparedStatement updateExperStat = null;
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            String updateExp = "UPDATE `experiments_table`  SET  `name`=? ,`fractions_number`=? ,`ready` =?,`uploaded_by`=?, `peptide_file`=?,`pblication_link`=?, `peptides_number`=? ,`proteins_number`=? ,`fraction_range`=? WHERE  `exp_id`=? ";
            updateExperStat = conn2.prepareStatement(updateExp);



            updateExperStat.setString(1, exp.getName().toUpperCase());
            updateExperStat.setInt(2, exp.getFractionsNumber());
            updateExperStat.setInt(3, 2);
            updateExperStat.setString(4, exp.getUploadedByName().toUpperCase());
            updateExperStat.setInt(5, exp.getPeptidesInclude());
            updateExperStat.setString(6, exp.getPublicationLink());
            updateExperStat.setInt(7, exp.getPeptidesNumber());
            updateExperStat.setInt(8, exp.getProteinsNumber());
            updateExperStat.setInt(9, 1);
            updateExperStat.setInt(10, exp.getExpId());
            int test = updateExperStat.executeUpdate();
            updateExperStat.close();
            if (test > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }

    }

    public synchronized boolean updateFractions(Connection conn2, FractionBean fb, int expId) {
        List<Integer> fractionIDs = this.getFractionIdsList(expId);
        for (int fractId : fractionIDs) {
            try {
                if (conn2 == null || conn2.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn2 = DriverManager.getConnection(url + dbName, userName, password);
                }
                String updateFraction = "UPDATE  `" + dbName + "`.`fractions_table` SET `number_peptides`=? ,`number_spectra`=? ,`average_ precursor_intensity`=?  WHERE `fraction_id` = ? AND `prot_accession`=?;";

                PreparedStatement updateFractionStat = null;
                for (ProteinBean fpb : fb.getProteinList().values()) {
                    boolean test = this.checkFractionProtine(fractId, fpb.getAccession());
                    if (test) {
                        updateFractionStat = conn2.prepareStatement(updateFraction);
                        updateFractionStat.setInt(1, fpb.getNumberOfPeptidePerFraction());
                        updateFractionStat.setInt(2, fpb.getNumberOfSpectraPerFraction());
                        updateFractionStat.setDouble(3, fpb.getAveragePrecursorIntensityPerFraction());
                        updateFractionStat.setInt(4, fb.getFractionId());
                        updateFractionStat.setString(5, fpb.getAccession().toUpperCase());
                        updateFractionStat.executeUpdate();
                    } else {
                        this.insertFraction(conn2, fb, expId);
                    }

                }

            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            }

        }

        return false;


    }

    private synchronized boolean checkFractionProtine(int fractId, String accession) {
        PreparedStatement selectFractStat = null;

        String selectFractProt = "SELECT `number_spectra` FROM `fractions_table` WHERE `fraction_id`=? AND `prot_accession`=?;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractStat = conn.prepareStatement(selectFractProt);
            selectFractStat.setInt(1, fractId);
            selectFractStat.setString(2, accession);
            ResultSet rs = selectFractStat.executeQuery();
            while (rs.next()) {

                return true;
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean checkProteinExisting(int expId, String prot_accession)//check if the protein exist so we use update 
    {
        PreparedStatement selectExpStat = null;
        boolean test = false;
        String selectExpProt = "SELECT `exp_id`,`prot_accession` FROM `experiment_protein_table` where `exp_id`=? and `prot_accession` = ?";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpStat = conn.prepareStatement(selectExpProt);
            selectExpStat.setInt(1, expId);
            selectExpStat.setString(2, prot_accession);
            ResultSet rs = selectExpStat.executeQuery();
            while (rs.next()) {
                test = true;
            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return test;
    }

    public synchronized ExperimentBean readyExper(int expId) {
        PreparedStatement selectExpStat = null;

        String selectExpProt = "SELECT * FROM `experiments_table` WHERE `exp_id` = ?";

        ExperimentBean exp = new ExperimentBean();

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpStat = conn.prepareStatement(selectExpProt);
            selectExpStat.setInt(1, expId);
            ResultSet rs = selectExpStat.executeQuery();
            while (rs.next()) {

                int ready = rs.getInt("ready");
                exp.setReady(2);
                int fractionsNumber = rs.getInt("fractions_number");
                exp.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                exp.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                exp.setName(name);
                String species = rs.getString("species");
                exp.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                exp.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                exp.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                exp.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                exp.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                exp.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                exp.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                exp.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                exp.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                exp.setPeptidesNumber(peptidesNumber);
                exp.setExpId(expId);


            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return null;
        }//error
        return exp;




    }

    public synchronized int getFractionNumber(int expId) {

        PreparedStatement selectExpStat = null;
        int fractionNumber = 0;
        String selectExp = "SELECT `fractions_number` FROM `experiments_table` WHERE `exp_id` = ?;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpStat = conn.prepareStatement(selectExp);
            selectExpStat.setInt(1, expId);
            ResultSet rs = selectExpStat.executeQuery();
            while (rs.next()) {
                fractionNumber = rs.getInt("fractions_number");
            }

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return fractionNumber;
    }

    public synchronized boolean checkPeptideExisting(String sequence) {
        PreparedStatement selectExpStat = null;
        boolean test = false;
        String selectExpProt = "SELECT `sequence` FROM `proteins_peptides_table` WHERE `sequence`=?";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpStat = conn.prepareStatement(selectExpProt);
            selectExpStat.setString(1, sequence);
            ResultSet rs = selectExpStat.executeQuery();
            while (rs.next()) {
                test = true;
            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return test;
    }

    // RETRIVEING DATA
    public synchronized Map<Integer, ExperimentBean> getExperiments()//get experiments list
    {
        PreparedStatement selectExpListStat = null;
        Map<Integer, ExperimentBean> ExpList = new HashMap<Integer, ExperimentBean>();
        Map<Integer, ExperimentBean> tempExpList = new HashMap<Integer, ExperimentBean>();

        String selectselectExpList = "SELECT * FROM `experiments_table` ;";


        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpListStat = conn.prepareStatement(selectselectExpList);
            ResultSet rs = selectExpListStat.executeQuery();
            while (rs.next()) {
                ExperimentBean exp = new ExperimentBean();
                int ready = rs.getInt("ready");
                exp.setReady(ready);
                int fractionsNumber = rs.getInt("fractions_number");
                exp.setFractionsNumber(fractionsNumber);
                int fractionRange = rs.getInt("fraction_range");
                exp.setFractionRange(fractionRange);
                String uploadedBy = rs.getString("uploaded_by");
                exp.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                exp.setName(name);
                String species = rs.getString("species");
                exp.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                exp.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                exp.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                exp.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                exp.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                exp.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                exp.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                exp.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                exp.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                exp.setPeptidesNumber(peptidesNumber);
                int expId = rs.getInt("exp_id");
                exp.setExpId(expId);
                String desc = rs.getString("description");
                exp.setDescription(desc);
                int expType = rs.getInt("exp_type");
                exp.setExpType(expType);
                exp.setNumberValidProt(rs.getInt("valid_prot"));
                ExpList.put(exp.getExpId(), exp);
            }
            rs.close();
            for (int expId : ExpList.keySet()) {
                ExperimentBean exp = ExpList.get(expId);
                List<Integer> fractionIds = this.getFractionIdsList(expId);
                exp.setFractionIds(fractionIds);
                //List<Integer> peptideIds = this.getPeptidesIdsList(expId);
                //exp.setPeptidesIds(peptideIds);
                tempExpList.put(expId, exp);

            }
            ExpList.clear();
            ExpList.putAll(tempExpList);
            tempExpList.clear();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return null;
        }//error
        return ExpList;


    }

    public synchronized ExperimentBean getExperiment(int expId) {
        ExperimentBean exp = new ExperimentBean();
        exp.setExpId(expId);
        exp = this.getExpDetails(exp);
        exp.setFractionsList(this.getFractionsList(exp.getExpId()));
        exp.setProteinList(this.getExpProteinsList(expId));	   	//get protein details	
        exp.setPeptideList(this.getExpPeptides(expId));
        return exp;
    }

    private synchronized List<Integer> getFractionIdsList(int expId) {
        PreparedStatement selectExpFractionStat = null;
        String selectExpFraction = "SELECT `fraction_id` FROM `experiment_fractions_table` WHERE `exp_id`=?;";
        List<Integer> fractionList = new ArrayList<Integer>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpFractionStat = conn.prepareStatement(selectExpFraction);
            selectExpFractionStat.setInt(1, expId);
            ResultSet rs = selectExpFractionStat.executeQuery();
            while (rs.next()) {
                fractionList.add(rs.getInt("fraction_id"));
            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return fractionList;
    }

    private synchronized ExperimentBean getExpDetails(ExperimentBean exp) {
        PreparedStatement selectExpStat = null;
        String selectExp = "SELECT * FROM `experiments_table` WHERE `exp_id`=? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpStat = conn.prepareStatement(selectExp);
            selectExpStat.setInt(1, exp.getExpId());
            ResultSet rs = selectExpStat.executeQuery();
            while (rs.next()) {
                int ready = rs.getInt("ready");
                exp.setReady(ready);
                int fractionsNumber = rs.getInt("fractions_number");
                exp.setFractionsNumber(fractionsNumber);
                int fractionRange = rs.getInt("fraction_range");
                exp.setFractionRange(fractionRange);
                String uploadedBy = rs.getString("uploaded_by");
                exp.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                exp.setName(name);
                String species = rs.getString("species");
                exp.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                exp.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                exp.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                exp.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                exp.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                exp.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                exp.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                exp.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                exp.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                exp.setPeptidesNumber(peptidesNumber);
                String dec = rs.getString("description");
                exp.setDescription(dec);
                int expType = rs.getInt("exp_type");
                exp.setExpType(expType);
                exp.setNumberValidProt(rs.getInt("valid_prot"));
            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return exp;

    }

    public synchronized Map<Integer, FractionBean> getFractionsList(int expId) {
        Map<Integer, FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
        try {

            //get fractions id list
            PreparedStatement selectFractsListStat = null;
            double minRange = 0.0;
            double maxRange = 0.0;
            String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range`,`index` FROM `experiment_fractions_table` where `exp_id` = ? ORDER BY `fraction_id`";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractsListStat = conn.prepareStatement(selectFractList);
            selectFractsListStat.setInt(1, expId);
            ResultSet rs = selectFractsListStat.executeQuery();
            ArrayList<FractionBean> fractionIdList = new ArrayList<FractionBean>();
            FractionBean fb = null;
            while (rs.next()) {
                fb = new FractionBean();
                int fraction_id = rs.getInt("fraction_id");
                fb.setFractionId(fraction_id);
                minRange = rs.getDouble("min_range");
                fb.setMinRange(minRange);
                maxRange = rs.getDouble("max_range");
                fb.setMaxRange(maxRange);
                int index = rs.getInt("index");
                fb.setFractionIndex(index);
                fractionIdList.add(fb);

            }
            rs.close();

            //get fractions 
            PreparedStatement selectFractsStat = null;
            String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ?";

            for (FractionBean fb2 : fractionIdList) {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectFractsStat = conn.prepareStatement(selectFract);
                selectFractsStat.setInt(1, fb2.getFractionId());
                rs = selectFractsStat.executeQuery();
                Map<String, ProteinBean> proteinList = new HashMap<String, ProteinBean>();
                otherSymbols.setGroupingSeparator('.');
                df = new DecimalFormat("#.##", otherSymbols);

                while (rs.next()) {
                    ProteinBean pb = new ProteinBean();//fraction_id		  			
                    pb.setAccession(rs.getString("prot_accession"));
                    pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
                    pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
                    pb.setAveragePrecursorIntensityPerFraction(Double.valueOf(df.format(rs.getDouble("average_ precursor_intensity"))));
                    proteinList.put(pb.getAccession(), pb);
                }


                fb2.setProteinList(proteinList);
                fractionsList.put(fb2.getFractionId(), fb2);
                rs.close();

            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return fractionsList;


    }

    public synchronized Map<Integer, PeptideBean> getExpPeptides(int expId) {

        Map<Integer, PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();
        try {

            //get fractions id list
            PreparedStatement selectPeptideListStat = null;
            String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptideListStat = conn.prepareStatement(selectPeptideList);
            selectPeptideListStat.setInt(1, expId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int peptideId = rs.getInt("pep_id");
                peptideIdList.add(peptideId);

            }
            rs.close();

            //get fractions 
            PreparedStatement selectPeptidesStat = null;
            String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE `peptide_id` = ?;";
            int counter = 0;
            for (int pepId : peptideIdList) {

                PeptideBean pepb = new PeptideBean();
                pepb.setPeptideId(pepId);
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPeptidesStat = conn.prepareStatement(selectPeptide);
                selectPeptidesStat.setInt(1, pepId);
                rs = selectPeptidesStat.executeQuery();

                while (rs.next()) {
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
                    pepb.setFixedModification(rs.getString("fixed_modification"));
                    pepb.setPeptideId(pepId);
                    pepb.setProteinInference(rs.getString("protein_inference"));
                    pepb.setSequenceTagged(rs.getString("sequence_tagged"));
                    pepb.setEnzymatic(Boolean.valueOf(rs.getString("enzymatic")));
                    pepb.setValidated(Double.valueOf(rs.getDouble("validated")));
                    pepb.setStarred(Boolean.valueOf(rs.getString("starred")));

                    pepb.setGlycopatternPositions(rs.getString("glycopattern_position(s)"));
                    String str = rs.getString("deamidation_and_glycopattern");
                    if (str != null && !str.equals("")) {
                        pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
                    }
                    pepb.setLikelyNotGlycosite(Boolean.valueOf(rs.getString("likelyNotGlycosite")));

                    peptidesList.put(pepb.getPeptideId(), pepb);



                }
                rs.close();
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
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return peptidesList;
    }

    public synchronized Map<String, ProteinBean> getExpProteinsList(int expId) {
        Map<String, ProteinBean> proteinExpList = new HashMap<String, ProteinBean>();
        try {
            PreparedStatement selectProtExpStat = null;
            String selectProtExp = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProtExpStat = conn.prepareStatement(selectProtExp);
            selectProtExpStat.setInt(1, expId);
            ResultSet rs = selectProtExpStat.executeQuery();
            while (rs.next()) {
                ProteinBean temPb = new ProteinBean();
                String protKey = rs.getString("prot_key");
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
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));
                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));

                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setDescription(rs.getString("description"));
                if (temPb.getOtherProteins() == null || temPb.getOtherProteins().equals("")) {
                    proteinExpList.put(temPb.getAccession(), temPb);
                } else {
                    proteinExpList.put(temPb.getAccession() + "," + temPb.getOtherProteins(), temPb);
                }
            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return proteinExpList;
    }

    //REMOVE DATA
    public synchronized boolean removeExperiment(int expId) {

        PreparedStatement remExpStat = null;//done
        PreparedStatement getFractExpStat = null;//done
        PreparedStatement remFractStat = null;//done
        PreparedStatement remFractExpStat = null;//done
        PreparedStatement getPepExpStat = null;//done
        PreparedStatement remPepExpStat = null;//done
        PreparedStatement remPeptStat = null;//done
        PreparedStatement remProtStat = null;//done



        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            this.removeStandarPlot(expId);
            String remExp = "DELETE FROM `experiments_table`  WHERE  `exp_id`=? ";

            String remFract = "DELETE FROM `" + dbName + "`.`fractions_table`   WHERE  `fraction_id` =? ";

            remExpStat = conn.prepareStatement(remExp);
            remExpStat.setInt(1, expId);
            remExpStat.executeUpdate();


            String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getPepExpStat = conn.prepareStatement(selectPeptideList);
            getPepExpStat.setInt(1, expId);
            ResultSet rs = getPepExpStat.executeQuery();
            ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int peptideId = rs.getInt("pep_id");
                peptideIdList.add(peptideId);

            }

            rs.close();
            String selectPeptide = "DELETE FROM `proteins_peptides_table` WHERE  `peptide_id`=? ;";

            for (int pepId : peptideIdList) {


                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                remPeptStat = conn.prepareStatement(selectPeptide);
                remPeptStat.setInt(1, pepId);
                remPeptStat.executeUpdate();

            }

            String removePeptide = "DELETE FROM `experiment_peptides_table` WHERE  `exp_id` = ? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            remPepExpStat = conn.prepareStatement(removePeptide);
            remPepExpStat.setInt(1, expId);
            remPepExpStat.executeUpdate();

            String remExpPro = "DELETE FROM `" + dbName + "`.`experiment_protein_table`  WHERE  `exp_id`=? ";
            remProtStat = conn.prepareStatement(remExpPro);
            remProtStat.setInt(1, expId);
            remProtStat.executeUpdate();


            String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getFractExpStat = conn.prepareStatement(selectFractList);
            getFractExpStat.setInt(1, expId);
            rs = getFractExpStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                fractionIdList.add(fraction_id);

            }
            rs.close();

            for (int fb : fractionIdList) {
                remFractStat = conn.prepareStatement(remFract);
                remFractStat.setInt(1, fb);
                remFractStat.executeUpdate();
            }


            String removeFraction = "DELETE FROM `experiment_fractions_table`   WHERE `exp_id` = ? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            remFractExpStat = conn.prepareStatement(removeFraction);
            remFractExpStat.setInt(1, expId);
            remFractExpStat.executeUpdate();

            return true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }
    }

    public synchronized Map<Integer, ProteinBean> searchProtein(String accession, int expId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";
        boolean test = false;
        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id`=? AND  `prot_key` LIKE(?) AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id`=? AND `prot_key` LIKE(?);";
        }


        Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat.setString(2, "%" + accession + "%");
            selectProStat.setInt(1, expId);
            if (validatedOnly) {
                selectProStat.setString(3, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            while (rs.next()) {
                ProteinBean temPb = new ProteinBean();
                temPb.setExpId(expId);
                temPb.setAccession(accession);
                temPb.setDescription(rs.getString("description"));
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
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));

                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));
                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setProtGroupId(rs.getInt("prot_group_id"));
                protExpList.put(temPb.getProtGroupId(), temPb);
            }
            rs.close();
            return protExpList;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return null;
        }


    }

    public synchronized Map<Integer, PeptideBean> getPeptidesProtList(Set<Integer> peptideIds) {

        ResultSet rs = null;
        // peptideIds.add(5741);
        Map<Integer, PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();
        try {

            PreparedStatement selectPeptidesStat = null;
            String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE  `peptide_id`=? ;";

            for (int pepId : peptideIds) {

                PeptideBean pepb = new PeptideBean();
                pepb.setPeptideId(pepId);
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPeptidesStat = conn.prepareStatement(selectPeptide);
                selectPeptidesStat.setInt(1, pepId);
                rs = selectPeptidesStat.executeQuery();


                while (rs.next()) {
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
                    pepb.setFixedModification(rs.getString("fixed_modification"));
                    pepb.setVariableModification(rs.getString("variable_modification"));
                    pepb.setProteinInference(rs.getString("protein_inference"));
                    pepb.setSequenceTagged(rs.getString("sequence_tagged"));
                    pepb.setEnzymatic(Boolean.valueOf(rs.getString("enzymatic")));
                    pepb.setValidated(Double.valueOf(rs.getDouble("validated")));
                    pepb.setStarred(Boolean.valueOf(rs.getString("starred")));
                    pepb.setPeptideId(pepId);
                    pepb.setGlycopatternPositions(rs.getString("glycopattern_position(s)"));
                    String str = rs.getString("deamidation_and_glycopattern");
                    if (str != null && !str.equals("")) {
                        pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
                    }

                    pepb.setLikelyNotGlycosite(Boolean.valueOf(rs.getString("likelyNotGlycosite")));


                    peptidesList.put(pepb.getPeptideId(), pepb);

                }
                rs.close();
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return peptidesList;
    }

    public synchronized Map<Integer, FractionBean> getProteinFractionList(String accession, int expId) {
        Map<Integer, FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
        try {

            //get fractions id list
            PreparedStatement selectFractsListStat = null;
            double minRange = 0.0;
            double maxRange = 0.0;
            int index = 0;
            String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range`,`index` FROM `experiment_fractions_table` where `exp_id` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractsListStat = conn.prepareStatement(selectFractList);
            selectFractsListStat.setInt(1, expId);
            ResultSet rs = selectFractsListStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                minRange = rs.getDouble("min_range");
                maxRange = rs.getDouble("max_range");
                index = rs.getInt("index");
                fractionIdList.add(fraction_id);

            }
            rs.close();
            //get fractions 
            PreparedStatement selectFractsStat = null;
            String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ? AND `prot_accession` =? ";

            for (int fractId : fractionIdList) {

                FractionBean fb = new FractionBean();
                fb.setMinRange(minRange);
                fb.setMaxRange(maxRange);
                fb.setFractionId(fractId);
                fb.setFractionIndex(index);
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectFractsStat = conn.prepareStatement(selectFract);
                selectFractsStat.setInt(1, fractId);
                selectFractsStat.setString(2, accession);
                rs = selectFractsStat.executeQuery();
                Map<String, ProteinBean> proteinList = new HashMap<String, ProteinBean>();
                otherSymbols.setGroupingSeparator('.');
                df = new DecimalFormat("#.##", otherSymbols);
                while (rs.next()) {
                    ProteinBean pb = new ProteinBean();//fraction_id		  			
                    pb.setAccession(rs.getString("prot_accession"));
                    pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
                    pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));

                    pb.setAveragePrecursorIntensityPerFraction(Double.valueOf(df.format(rs.getDouble("average_ precursor_intensity"))));
                    proteinList.put(pb.getAccession(), pb);
                }

                fb.setProteinList(proteinList);
                fractionsList.put(fb.getFractionId(), fb);
                rs.close();

            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return fractionsList;


    }

    public synchronized Map<Integer, ProteinBean> searchProteinByName(String protSearch, int expId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";
        Map<Integer, ProteinBean> proteinsList = new HashMap<Integer, ProteinBean>();

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE `description` LIKE (?) AND `exp_id`=? AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE `description` LIKE (?) AND `exp_id`=? ";
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat.setString(1, "%" + protSearch + "%");
            selectProStat.setInt(2, expId);
            if (validatedOnly) {
                selectProStat.setString(3, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            int in = 1;
            while (rs.next()) {
                ProteinBean temPb = new ProteinBean();
                temPb.setExpId(expId);
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setDescription(rs.getString("description"));
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
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));

                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));
                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setProtGroupId(rs.getInt("prot_group_id"));
                proteinsList.put(temPb.getProtGroupId(), temPb);

            }
            rs.close();
            return proteinsList;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }

        return null;
    }

    public synchronized Map<Integer, ProteinBean> searchProteinByPeptideSequence(String protSearch, int expId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        PreparedStatement selectPepIdStat = null;
        Map<Integer, ProteinBean> proteinsList = new HashMap<Integer, ProteinBean>();
        List<Integer> pepIdList = new ArrayList<Integer>();
        String selectPepId = "SELECT `peptide_id`  FROM `proteins_peptides_table` WHERE `exp_id` = ? AND `sequence` = ? ;";
        Set<String> accessionList = new HashSet<String>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPepIdStat = conn.prepareStatement(selectPepId);
            selectPepIdStat.setInt(1, expId);
            selectPepIdStat.setString(2, protSearch);
            ResultSet rs = selectPepIdStat.executeQuery();
            while (rs.next()) {
                pepIdList.add(rs.getInt("peptide_id"));
            }
            rs.close();

            String selectPro = "SELECT `protein`  FROM `experiment_peptides_proteins_table`  WHERE `exp_id` = ? AND `peptide_id` = ? ;";

            for (int key : pepIdList) {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectProStat = conn.prepareStatement(selectPro);
                selectProStat.setInt(1, expId);
                selectProStat.setInt(2, key);
                ResultSet rs_ = selectProStat.executeQuery();
                while (rs_.next()) {
                    accessionList.add(rs_.getString("protein"));
                }
                rs_.close();

                for (String accKey : accessionList) {
                    String[] AccArr = accKey.split(",");
                    for (String str : AccArr) {
                        if (str.length() > 3) {
                            Map<Integer, ProteinBean> tempProteinsList = this.searchProtein(str.trim(), expId, validatedOnly);
                            if (tempProteinsList != null) {
                                proteinsList.putAll(tempProteinsList);
                            }
                        }
                    }
                }

            }
            return proteinsList;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }

        return null;
    }

    private synchronized boolean checkPeptideExp(int key, int expId) {
        PreparedStatement selectPepExpStat = null;
        boolean test = false;
        String selectPepExp = "SELECT * FROM `experiment_peptides_table`  WHERE `exp_id` = ? AND `pep_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPepExpStat = conn.prepareStatement(selectPepExp);
            selectPepExpStat.setInt(1, expId);
            selectPepExpStat.setInt(2, key);
            ResultSet rs = selectPepExpStat.executeQuery();
            while (rs.next()) {
                test = true;
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }

        return test;
    }

    //Security Handling 
    public synchronized boolean regUser(String username, String password2, boolean admin, String email) {
        PreparedStatement regUserStat = null;
        boolean test = false;
        String insertUser = "INSERT INTO  `" + dbName + "`.`users_table`(`user_name`,`password`,`admin`,`email`) VALUES (?,?,?,?);";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            regUserStat = conn.prepareStatement(insertUser);
            regUserStat.setString(1, username.toUpperCase());
            regUserStat.setString(2, password2);
            regUserStat.setString(3, "" + admin);
            regUserStat.setString(4, email);
            int rs = regUserStat.executeUpdate();
            if (rs > 0) {
                test = true;
            }

        } catch (Exception e) {
        }

        return test;
    }

    public synchronized boolean validateUsername(String email) {
        try {

            //get username
            PreparedStatement selectUserStat = null;
            String selectuser = "SELECT * FROM `users_table` WHERE `email` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                return false;//valid username

            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return true;//not valid
    }

    public synchronized String authenticate(String email) {
        //get password 
        PreparedStatement selectUserStat = null;
        try {
            String selectuser = "SELECT `password` FROM `users_table` WHERE `email` =  ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                return rs.getString("password");//valid username

            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return null;//not valid
    }

    public synchronized User getUser(String email) {
        PreparedStatement selectUserStat = null;

        try {
            String selectuser = "SELECT * FROM `users_table` WHERE `email` =  ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setAdmin(Boolean.valueOf(rs.getString("admin")));
                user.setUsername(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                return user;

            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return null;//not valid
    }

    public synchronized Map<Integer, String> getUsersList() {
        Map<Integer, String> usersList = new HashMap<Integer, String>();
        PreparedStatement selectUsersStat = null;

        try {
            String selectusers = "SELECT * FROM `users_table` WHERE `admin` = 'false'; ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUsersStat = conn.prepareStatement(selectusers);
            ResultSet rs = selectUsersStat.executeQuery();
            while (rs.next()) {

                usersList.put(rs.getInt("id"), rs.getString("user_name"));


            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return usersList;//not valid
    }

    public synchronized boolean removeUser(String user) {
        try {


            PreparedStatement removeUserStat = null;
            String removeuser = "DELETE  FROM `users_table` WHERE `user_name` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            removeUserStat = conn.prepareStatement(removeuser);
            removeUserStat.setString(1, user.toUpperCase());
            int rs = removeUserStat.executeUpdate();
            if (rs > 0) {
                return true;//valid username

            }
            removeUserStat.clearParameters();
            removeUserStat.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return false;//not valid
    }

    public synchronized boolean updateUserPassword(String username2, String newpassword) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String updateProtDesc = "UPDATE  `" + dbName + "`.`users_table` SET `password`= ? WHERE `user_name` = ? ;";
            PreparedStatement updateProtDescStat = conn.prepareStatement(updateProtDesc);
            updateProtDescStat.setString(1, newpassword);
            updateProtDescStat.setString(2, username2.toUpperCase());
            int test = updateProtDescStat.executeUpdate();
            updateProtDescStat.clearParameters();
            updateProtDescStat.close();
            if (test > 0) {
                return true;
            }
        } catch (Exception e2) {
            System.err.println(e2.getLocalizedMessage());
//            e2.printStackTrace();
        }

        return false;
    }

    public boolean updateFractionRange(ExperimentBean exp) {
        List<Integer> fractionIDs = this.getFractionIdsList(exp.getExpId());
        java.util.Collections.sort(fractionIDs);
        Map<Integer, FractionBean> fractionRangeList = exp.getFractionsList();
        int x = 1;
        String updateFraction = "UPDATE  `" + dbName + "`.`experiment_fractions_table` SET `min_range`=? ,`max_range`=?,`index`=? WHERE `fraction_id`=?;";
        PreparedStatement updateFractionStat = null;
        FractionBean fb = null;


        for (int fractId : fractionIDs) {
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                fb = fractionRangeList.get(x);
                updateFractionStat = conn.prepareStatement(updateFraction);
                updateFractionStat.setDouble(1, fb.getMinRange());
                updateFractionStat.setDouble(2, fb.getMaxRange());
                updateFractionStat.setInt(3, fb.getFractionIndex());
                updateFractionStat.setInt(4, fractId);
                updateFractionStat.executeUpdate();

                updateFractionStat.clearParameters();
                updateFractionStat.close();
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            }
            x++;


        }

        boolean test = true;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            exp.setFractionRange(1);
            test = updateExperiment(conn, exp);	//update exp table

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            test = false;
        }
        return test;
    }

    public synchronized boolean removeFractions(Connection conn2, int expId) {
        PreparedStatement getFractExpStat = null;//done
        PreparedStatement remFractStat = null;//done
        PreparedStatement remFractExpStat = null;//done

        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            String remFract = "DELETE FROM `" + dbName + "`.`fractions_table`   WHERE  `fraction_id` =? ";
            String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";

            getFractExpStat = conn2.prepareStatement(selectFractList);
            getFractExpStat.setInt(1, expId);
            ResultSet rs = getFractExpStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                fractionIdList.add(fraction_id);

            }

            for (int fb : fractionIdList) {
                remFractStat = conn2.prepareStatement(remFract);
                remFractStat.setInt(1, fb);
                remFractStat.executeUpdate();
            }

            rs.close();

            String removeFraction = "DELETE FROM `experiment_fractions_table`   WHERE `exp_id` = ? ;";

            remFractExpStat = conn2.prepareStatement(removeFraction);
            remFractExpStat.setInt(1, expId);
            remFractExpStat.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean insertFractions(Connection conn2, ExperimentBean exp) {
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }

            String insertFractExp = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            PreparedStatement insertFractExpStat = conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
            int fractId = 0;

            for (FractionBean fb : exp.getFractionsList().values()) {
                insertFractExpStat = conn2.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
                insertFractExpStat.setInt(1, exp.getExpId());
                insertFractExpStat.setDouble(2, fb.getMinRange());
                insertFractExpStat.setDouble(3, fb.getMaxRange());
                insertFractExpStat.setInt(4, fb.getFractionIndex());
                insertFractExpStat.executeUpdate();
                ResultSet rs = insertFractExpStat.getGeneratedKeys();
                while (rs.next()) {
                    fractId = rs.getInt(1);
                }
                for (ProteinBean pb : fb.getProteinList().values()) {
                    this.insertProteinFract(conn2, fractId, pb);
                }
                rs.close();
            }

        } catch (Exception exc) {
            System.err.println(exc.getLocalizedMessage());
//            exc.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized boolean updateProtFractionFile(ExperimentBean tempExp, ExperimentBean exp) {
        boolean test = true;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            if (tempExp.getReady() == 1 && tempExp.getFractionsNumber() == 0)//we need to update ready number to 2 -- previous file was protein fraction file
            {
                tempExp.setFractionsNumber(exp.getFractionsNumber());
                tempExp.setReady(2);
                test = updateExperiment(conn, tempExp);	//update exp table
                for (FractionBean fb : exp.getFractionsList().values()) {
                    insertFraction(conn, fb, exp.getExpId());// update fraction-exp table and fraction table
                }
            } else {
                tempExp.setFractionsNumber(exp.getFractionsNumber());
                tempExp.setReady(2);
                test = updateExperiment(conn, tempExp);	//update exp table
                //remove all fractions
                test = removeFractions(conn, exp.getExpId());
                insertFractions(conn, exp);

            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            test = false;
        }
        return test;
    }

    public boolean setStandardPlotProt(ExperimentBean exp) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updateProtExpFraction(ProteinBean pb, int expId) {

        String updateProtFraction = "UPDATE  `" + dbName + "`.`experiment_protein_table` SET `spectrum_fraction_spread_upper_range_kDa`=? ,`spectrum_fraction_spread_lower_range_kDa`=? ,`peptide_fraction_spread_upper_range_kDa`=? , `peptide_fraction_spread_lower_range_kDa`=?   WHERE `exp_id` = ? AND `prot_accession`=?;";
        boolean test = false;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updateProtFractionStat = conn.prepareStatement(updateProtFraction);
            updateProtFractionStat.setString(1, pb.getSpectrumFractionSpread_upper_range_kDa());
            updateProtFractionStat.setString(2, pb.getSpectrumFractionSpread_lower_range_kDa());
            updateProtFractionStat.setString(3, pb.getPeptideFractionSpread_upper_range_kDa());
            updateProtFractionStat.setString(4, pb.getPeptideFractionSpread_lower_range_kDa());
            updateProtFractionStat.setInt(5, expId);
            updateProtFractionStat.setString(6, pb.getAccession());
            updateProtFractionStat.executeUpdate();
            test = true;
            updateProtFractionStat.clearParameters();
            updateProtFractionStat.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            test = false;
        }
        return test;

    }

    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds) {

        Map<Integer, PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement selectPeptidesStat = null;
            String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE  `peptide_id`=? ;";

            for (int pepId : peptideIds) {

                PeptideBean pepb = new PeptideBean();
                pepb.setPeptideId(pepId);
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPeptidesStat = conn.prepareStatement(selectPeptide);
                selectPeptidesStat.setInt(1, pepId);
                ResultSet rs = selectPeptidesStat.executeQuery();


                while (rs.next()) {
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
                    pepb.setFixedModification(rs.getString("fixed_modification"));
                    pepb.setVariableModification(rs.getString("variable_modification"));
                    pepb.setProteinInference(rs.getString("protein_inference"));
                    pepb.setSequenceTagged(rs.getString("sequence_tagged"));
                    pepb.setEnzymatic(Boolean.valueOf(rs.getString("enzymatic")));
                    pepb.setValidated(Double.valueOf(rs.getDouble("validated")));
                    pepb.setStarred(Boolean.valueOf(rs.getString("starred")));
                    pepb.setPeptideId(pepId);
                    pepb.setGlycopatternPositions(rs.getString("glycopattern_position(s)"));
                    String str = rs.getString("deamidation_and_glycopattern");
                    if (str != null && !str.equals("")) {
                        pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
                    }
                    pepb.setLikelyNotGlycosite(Boolean.valueOf(rs.getString("likelyNotGlycosite")));

                    if (pepb.getDecoy() != 1) {
                        peptidesList.put(pepb.getPeptideId(), pepb);
                    }
                }
                rs.close();
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return peptidesList;
    }

    public int insertExpProtPept(int expId, int pepId, String accession, Connection conn2) {
        int test = -1;
        try {

            if (conn2 == null || conn2.isClosed()) {
                Class.forName(driver).newInstance();
                conn2 = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertExpProtPeptQ = "INSERT INTO  `" + dbName + "`.`experiment_peptides_proteins_table` (`exp_id` ,`peptide_id`,`protein`)VALUES (?,?,?);";
            PreparedStatement insertExpProtPeptQStat = conn2.prepareStatement(insertExpProtPeptQ);
            insertExpProtPeptQStat.setInt(1, expId);
            insertExpProtPeptQStat.setInt(2, pepId);
            insertExpProtPeptQStat.setString(3, accession.toUpperCase());
            test = insertExpProtPeptQStat.executeUpdate();
            insertExpProtPeptQStat.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }

        return test;
    }

    public Set<Integer> getExpPepProIds(int expId, String accession) {
        PreparedStatement selectExpProPepStat = null;
        String selectExpProPep = "SELECT `peptide_id` FROM `experiment_peptides_proteins_table` WHERE `exp_id`=? AND `protein` LIKE(?)";
        Set<Integer> expProPepIds = new HashSet<Integer>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectExpProPepStat = conn.prepareStatement(selectExpProPep);
            selectExpProPepStat.setInt(1, expId);
            selectExpProPepStat.setString(2, "%" + accession.toUpperCase() + "%");
            ResultSet rs = selectExpProPepStat.executeQuery();
            while (rs.next()) {
                expProPepIds.add(rs.getInt("peptide_id"));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return expProPepIds;
    }

    public boolean updateStandardPlotProt(ExperimentBean exp) {
        removeStandarPlot(exp.getExpId());
        for (StandardProteinBean spb : exp.getStanderdPlotProt()) {
            insertStandardPlotProtein(exp.getExpId(), spb);
        }
        return true;
    }

    public boolean insertStandardPlotProtein(int expId, StandardProteinBean spb) {
        int check = -1;
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertStandPlotQ = "INSERT INTO  `" + dbName + "`.`standard_plot_proteins`(`exp_id` ,`mw_(kDa)`,`name`,`lower`,`upper`,`color`)VALUES (?,?,?,?,?,?);";
            PreparedStatement insertStandPlotStat = conn.prepareStatement(insertStandPlotQ);
            insertStandPlotStat.setInt(1, expId);
            insertStandPlotStat.setDouble(2, spb.getMW_kDa());
            insertStandPlotStat.setString(3, spb.getName().toUpperCase());
            insertStandPlotStat.setInt(4, spb.getLowerFraction());
            insertStandPlotStat.setInt(5, spb.getUpperFraction());
            insertStandPlotStat.setString(6, spb.getColor().toUpperCase());
            check = insertStandPlotStat.executeUpdate();
            insertStandPlotStat.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }
        if (check > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean removeStandarPlot(int expId) {
        int x = 0;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String StandarPlot = "DELETE FROM `standard_plot_proteins`  WHERE  `exp_id`=? ";


            PreparedStatement remExpStat = conn.prepareStatement(StandarPlot);
            remExpStat.setInt(1, expId);
            x = remExpStat.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        if (x > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<StandardProteinBean> getStandardProtPlotList(int expId) {
        List<StandardProteinBean> standardPlotList = new ArrayList<StandardProteinBean>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String selectPeptide = "SELECT * FROM `standard_plot_proteins` where `exp_id` = ?;";
            PreparedStatement selectPeptideStat = conn.prepareStatement(selectPeptide);
            selectPeptideStat.setInt(1, expId);
            ResultSet rs = selectPeptideStat.executeQuery();
            while (rs.next()) {
                StandardProteinBean spb = new StandardProteinBean();
                spb.setMW_kDa(Double.valueOf(rs.getDouble("mw_(kDa)")));
                spb.setLowerFraction(rs.getInt("lower"));
                spb.setUpperFraction(rs.getInt("upper"));
                spb.setName(rs.getString("name"));
                spb.setColor(rs.getString("color"));
                standardPlotList.add(spb);

            }
            rs.close();
        } catch (Exception exp) {
            System.err.println(exp.getLocalizedMessage());
//            exp.printStackTrace();
            return null;
        }



        return standardPlotList;
    }

    public boolean updateExpData(ExperimentBean exp) {

        String updateExp = "UPDATE  `" + dbName + "`.`experiments_table`  SET `name`=?,`ready`=? ,`uploaded_by`=?,`species`=?,`sample_type`=?,`sample_processing`=?,`instrument_type`=?,`frag_mode` =?,`proteins_number` = ? ,	`email` =?,`pblication_link`=?,`description`=?  WHERE `exp_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updateExpStat = conn.prepareStatement(updateExp, Statement.RETURN_GENERATED_KEYS);
            updateExpStat.setString(1, exp.getName().toUpperCase());
            updateExpStat.setInt(2, 2);
            updateExpStat.setString(3, exp.getUploadedByName().toUpperCase());
            updateExpStat.setString(4, exp.getSpecies());
            updateExpStat.setString(5, exp.getSampleType());
            updateExpStat.setString(6, exp.getSampleProcessing());
            updateExpStat.setString(7, exp.getInstrumentType());
            updateExpStat.setString(8, exp.getFragMode());
            updateExpStat.setInt(9, exp.getProteinsNumber());
            updateExpStat.setString(10, exp.getEmail().toUpperCase());
            if (exp.getPublicationLink() != null) {
                updateExpStat.setString(11, exp.getPublicationLink());
            } else {
                updateExpStat.setString(11, "NOT AVAILABLE");
            }
            updateExpStat.setString(12, exp.getDescription());
            updateExpStat.setInt(13, exp.getExpId());
            int test = updateExpStat.executeUpdate();
            if (test > 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }

        return false;
    }

    //    public synchronized boolean setProteinFile(ExperimentBean exp) {
//        if (exp.getExpId() == -1)//new Experiment
//        {
//            PreparedStatement insertExpStat = null;
//            int id = 0;
//            int test = 0;
//
//            String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready` ,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`proteins_number` ,	`email` ,`pblication_link`,`description`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ;";
//            try {
//                if (conn == null || conn.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                insertExpStat = conn.prepareStatement(insertExp, Statement.RETURN_GENERATED_KEYS);
//                insertExpStat.setString(1, exp.getName().toUpperCase());
//                insertExpStat.setInt(2, 1);
//                insertExpStat.setString(3, exp.getUploadedByName().toUpperCase());
//                insertExpStat.setString(4, exp.getSpecies());
//                insertExpStat.setString(5, exp.getSampleType());
//                insertExpStat.setString(6, exp.getSampleProcessing());
//                insertExpStat.setString(7, exp.getInstrumentType());
//                insertExpStat.setString(8, exp.getFragMode());
//                insertExpStat.setInt(9, exp.getProteinsNumber());
//                insertExpStat.setString(10, exp.getEmail().toUpperCase());
//                if (exp.getPublicationLink() != null) {
//                    insertExpStat.setString(11, exp.getPublicationLink());
//                } else {
//                    insertExpStat.setString(11, "NOT AVAILABLE");
//                }
//                insertExpStat.setString(12, exp.getDescription());
//                insertExpStat.executeUpdate();
//                ResultSet rs = insertExpStat.getGeneratedKeys();
//                while (rs.next()) {
//                    id = rs.getInt(1);
//                }
//                for (ProteinBean pb : exp.getProteinList().values()) {
//                    test = this.insertProteinExper(conn, driver, url, dbName, userName, password, id, pb);
//                    test = this.insertProt(conn, pb.getAccession(), pb.getDescription());
//                }
//                // 
//            } catch (Exception e) {
//                System.err.println(e.getLocalizedMessage());
//                return false;
//            }
//
//            if (test > 0) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    public synchronized boolean setPeptideFile(ExperimentBean exp) {
//        if (exp.getExpId() == -1)//new Experiment
//        {
//            PreparedStatement insertExpStat = null;
//            PreparedStatement insertPeptExpStat = null;
//            int expId = 0;
//            int PepId = 0;
//            int test = 0;
//            String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready`,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`fractions_number` ,	`email` ,`pblication_link`,`peptide_file`,`peptides_number`,`description`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
//            String insertPeptideExp = "INSERT INTO  `" + dbName + "`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
//            try {
//                if (conn == null || conn.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                insertExpStat = conn.prepareStatement(insertExp, Statement.RETURN_GENERATED_KEYS);
//                insertExpStat.setString(1, exp.getName().toUpperCase());
//                insertExpStat.setInt(2, 0);
//                insertExpStat.setString(3, exp.getUploadedByName().toUpperCase());
//
//                insertExpStat.setString(4, exp.getSpecies());
//
//                insertExpStat.setString(5, exp.getSampleType());
//
//                insertExpStat.setString(6, exp.getSampleProcessing());
//
//                insertExpStat.setString(7, exp.getInstrumentType());
//
//                insertExpStat.setString(8, exp.getFragMode());
//
//                insertExpStat.setInt(9, 0);
//                insertExpStat.setString(10, exp.getEmail().toUpperCase());
//                if (exp.getPublicationLink() != null) {
//                    insertExpStat.setString(11, exp.getPublicationLink().toUpperCase());
//                } else {
//                    insertExpStat.setString(11, "NOT AVAILABLE");
//                }
//                insertExpStat.setInt(12, 1);
//                insertExpStat.setInt(13, exp.getPeptidesNumber());
//                insertExpStat.setString(14, exp.getDescription());
//                insertExpStat.executeUpdate();
//                ResultSet rs = insertExpStat.getGeneratedKeys();
//                while (rs.next()) {
//                    expId = rs.getInt(1);
//                }
//                rs.close();
//                int counter = 0;
//                for (PeptideBean pepb : exp.getPeptideList().values()) {
//                    insertPeptExpStat = conn.prepareStatement(insertPeptideExp, Statement.RETURN_GENERATED_KEYS);
//                    insertPeptExpStat.setInt(1, expId);
//                    insertPeptExpStat.executeUpdate();
//                    rs = insertPeptExpStat.getGeneratedKeys();
//                    while (rs.next()) {
//                        PepId = rs.getInt(1);
//
//                    }
//                    insertPeptExpStat.clearParameters();
//                    rs.close();
//                    test = this.insertPeptide(conn, PepId, pepb, expId);
//
//                    counter++;
//                    if (counter == 10000) {
//                        conn.close();
//
//                        Thread.sleep(100);
//                        Class.forName(driver).newInstance();
//
//                        conn = DriverManager.getConnection(url + dbName, userName, password);
//                        counter = 0;
//                    }
//                }
//
//            } catch (Exception e) {
//                System.err.println(e.getLocalizedMessage());
//                return false;
//            }
//            if (test > 0) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    private synchronized int insertProteinExper(Connection conn2, String driver, String url, String dbName, String userName, String password, int expId, ProteinBean pb) {
//        int test = -1;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String insertProtExp = "INSERT INTO  `" + dbName + "`.`experiment_protein_table` (`exp_id` ,`prot_accession` ,`other_protein(s)` ,`protein_inference_class` ,`sequence_coverage(%)` ,`observable_coverage(%)` ,`confident_ptm_sites` ,`number_confident` ,`other_ptm_sites` ,`number_other` ,`number_validated_peptides` ,`number_validated_spectra` ,`em_pai` ,`nsaf` ,`mw_(kDa)` ,`score` ,`confidence` ,`starred`,`non_enzymatic_peptides`)VALUES (?,?,?,  ?, ?, ?, ?,  ?,  ?, ?, ?, ?, ?,  ?, ?,?,?,?,?);";
//            PreparedStatement insertProtStat = conn2.prepareStatement(insertProtExp, Statement.RETURN_GENERATED_KEYS);
//            insertProtStat.setInt(1, expId);
//            insertProtStat.setString(2, pb.getAccession().toUpperCase());
//            insertProtStat.setString(3, pb.getOtherProteins().toUpperCase());
//            insertProtStat.setString(4, pb.getProteinInferenceClass().toUpperCase());
//            insertProtStat.setDouble(5, pb.getSequenceCoverage());
//            insertProtStat.setDouble(6, pb.getObservableCoverage());
//            insertProtStat.setString(7, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
//            insertProtStat.setString(8, pb.getNumberConfident().toString());
//            insertProtStat.setString(9, pb.getOtherPtmSites().toUpperCase());
//            insertProtStat.setString(10, pb.getNumberOfOther().toUpperCase());
//            insertProtStat.setInt(11, pb.getNumberValidatedPeptides());
//            insertProtStat.setInt(12, pb.getNumberValidatedSpectra());
//            insertProtStat.setDouble(13, pb.getEmPai());
//            insertProtStat.setDouble(14, pb.getNsaf());
//            insertProtStat.setDouble(15, pb.getMw_kDa());
//            insertProtStat.setDouble(16, pb.getScore());
//            insertProtStat.setDouble(17, pb.getConfidence());
//            insertProtStat.setString(18, String.valueOf(pb.isStarred()));
//            insertProtStat.setString(19, (String.valueOf(pb.isNonEnzymaticPeptides()).toUpperCase()));
//            test = insertProtStat.executeUpdate();
//            insertProtStat.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//
//        return test;
//    }
//    private synchronized int insertProt(Connection conn2, String accession, String desc)//fill protein table
//    {
//        int test = -1;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String insertProt = "INSERT INTO  `" + dbName + "`.`proteins_table` (`accession` ,`description`)VALUES (?,?);";
//            PreparedStatement insertProtStat = conn2.prepareStatement(insertProt, Statement.RETURN_GENERATED_KEYS);
//            insertProtStat.setString(1, accession.toUpperCase());
//            insertProtStat.setString(2, desc.toUpperCase());
//            test = insertProtStat.executeUpdate();
//            insertProtStat.close();
//        } catch (Exception e) {
//            test = updateProt(conn2, driver, url, dbName, userName, password, accession, desc);
//        }
//        return test;
//    }
    
//    public synchronized int insertProtDescription(Connection conn2, String accession, String description) {
//
//        int test = -1;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String insertProtDesc = "INSERT INTO  `" + dbName + "`.`proteins_table` (`accession` ,`description`)VALUES (?,?);";
//            PreparedStatement insertProtDescStat = conn2.prepareStatement(insertProtDesc);
//            insertProtDescStat.setString(1, accession.toUpperCase());
//            insertProtDescStat.setString(2, description.toUpperCase());
//            test = insertProtDescStat.executeUpdate();
//            insertProtDescStat.close();
//
//        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e)//in case of protein existence so update description
//        {
//
//            try {
//                if (conn2 == null || conn2.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn2 = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                String updateProtDesc = "UPDATE  `" + dbName + "`.`proteins_table` SET `description`=? WHERE `accession` = ? ;";
//                PreparedStatement updateProtDescStat = conn2.prepareStatement(updateProtDesc);
//                updateProtDescStat.setString(2, accession.toUpperCase());
//                updateProtDescStat.setString(1, description.toUpperCase());
//                test = updateProtDescStat.executeUpdate();
//                updateProtDescStat.close();
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return test;
//    }
//    public synchronized int insertPeptide(Connection conn2, int pepId, PeptideBean pepb, int expId) {
//        String insertPeptide = "INSERT INTO  `" + dbName + "`.`proteins_peptides_table` (`protein` ,`other_protein(s)` ,`peptide_protein(s)` ,`other_protein_description(s)` ,`peptide_proteins_description(s)` ,`aa_before` ,`sequence` ,"
//                + "`aa_after` ,`peptide_start` ,`peptide_end` ,`variable_modification` ,`location_confidence` ,`precursor_charge(s)` ,`number_of_validated_spectra` ,`score` ,`confidence` ,`peptide_id`,`fixed_modification`,`protein_inference`,`sequence_tagged`,`enzymatic`,`validated`,`starred`,`glycopattern_position(s)`,`deamidation_and_glycopattern` )VALUES ("
//                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?);";
//        if (pepId == -1)//generate peptide id
//        {
//            String insertPeptideExp = "INSERT INTO  `" + dbName + "`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
//            try {
//                if (conn2 == null || conn2.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn2 = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                PreparedStatement insertPeptExpStat = conn2.prepareStatement(insertPeptideExp, Statement.RETURN_GENERATED_KEYS);
//                insertPeptExpStat.setInt(1, expId);
//                insertPeptExpStat.executeUpdate();
//                ResultSet rs = insertPeptExpStat.getGeneratedKeys();
//                while (rs.next()) {
//                    pepId = rs.getInt(1);
//                }
//                rs.close();
//
//            } catch (Exception e) {
//                System.err.println(e.getLocalizedMessage());
////                e.printStackTrace();
//            }
//        }
//
//        int test = -1;
//        PreparedStatement insertPeptideStat = null;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            insertPeptideStat = conn2.prepareStatement(insertPeptide, Statement.RETURN_GENERATED_KEYS);
//            insertPeptideStat.setString(1, pepb.getProtein().toUpperCase());
//            insertPeptideStat.setString(2, pepb.getOtherProteins().toUpperCase());
//            insertPeptideStat.setString(3, pepb.getPeptideProteins().toUpperCase());
//            insertPeptideStat.setString(4, pepb.getOtherProteinDescriptions().toUpperCase());
//            insertPeptideStat.setString(5, pepb.getPeptideProteinsDescriptions().toUpperCase());
//            insertPeptideStat.setString(6, pepb.getAaBefore().toUpperCase());
//            insertPeptideStat.setString(7, pepb.getSequence().toUpperCase());
//            insertPeptideStat.setString(8, pepb.getAaAfter().toUpperCase());
//            insertPeptideStat.setString(9, pepb.getPeptideStart().toUpperCase());
//            insertPeptideStat.setString(10, pepb.getPeptideEnd().toUpperCase());
//            insertPeptideStat.setString(11, pepb.getVariableModification().toUpperCase());
//            insertPeptideStat.setString(12, pepb.getLocationConfidence().toUpperCase());
//            insertPeptideStat.setString(13, pepb.getPrecursorCharges().toUpperCase());
//            insertPeptideStat.setInt(14, pepb.getNumberOfValidatedSpectra());
//            insertPeptideStat.setDouble(15, pepb.getScore());
//            insertPeptideStat.setDouble(16, pepb.getConfidence());
//            insertPeptideStat.setInt(17, pepId);
//            insertPeptideStat.setString(18, pepb.getFixedModification().toUpperCase());
//            insertPeptideStat.setString(19, pepb.getProteinInference());
//            insertPeptideStat.setString(20, pepb.getSequenceTagged());
//            insertPeptideStat.setString(21, String.valueOf(pepb.isEnzymatic()).toUpperCase());
//            insertPeptideStat.setDouble(22, pepb.getValidated());
//            insertPeptideStat.setString(23, String.valueOf(pepb.isStarred()).toUpperCase());
//            if (pepb.getGlycopatternPositions() != null) {
//                insertPeptideStat.setString(24, pepb.getGlycopatternPositions());
//            } else {
//                insertPeptideStat.setString(24, null);
//            }
//            if (pepb.isDeamidationAndGlycopattern() != null && pepb.isDeamidationAndGlycopattern()) {
//                insertPeptideStat.setString(25, String.valueOf(pepb.isDeamidationAndGlycopattern()).toUpperCase());
//            } else {
//                insertPeptideStat.setString(25, "");
//            }
//
//            test = insertPeptideStat.executeUpdate();
//
//            insertPeptideStat.clearParameters();
//            insertPeptideStat.close();
//
//            insertExpProtPept(expId, pepId, pepb.getProtein().toUpperCase(), conn2);
//        } catch (Exception e) {
//            System.err.println(e.getLocalizedMessage());
////            e.printStackTrace();
//            try {
//                insertPeptideStat.close();
//            } catch (SQLException e1) {
////                e1.printStackTrace();
//                System.err.println(e1.getLocalizedMessage());
//            }
//        }
//
//        return test;
//    }

//    public synchronized boolean addProtine(Connection conn2, ProteinBean pb, int expId) {
//
//        int test = this.insertProteinExper(conn2, driver, url, dbName, userName, password, expId, pb);
//        test = this.insertProt(conn2, pb.getAccession(), pb.getDescription());
//        if (test > 0) {
//            return true;
//        }
//
//        return false;
//    }
//    private synchronized int updateProt(Connection conn2, String driver, String url, String dbName, String userName, String password, String accession, String desc)//fill protein table
//    {
//        int test = -1;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String insertProt = "UPDATE  `" + dbName + "`.`proteins_table` SET `description` = ? WHERE `accession`=?;";
//            PreparedStatement insertProtStat = conn2.prepareStatement(insertProt, Statement.RETURN_GENERATED_KEYS);
//            insertProtStat.setString(1, desc.toUpperCase());
//            insertProtStat.setString(2, accession.toUpperCase());
//            test = insertProtStat.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return test;
//    }
//    public synchronized boolean updateProtein(Connection conn2, ProteinBean pb, int expId) {
//        PreparedStatement updateProtStat = null;
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String updateProt = "UPDATE `" + dbName + "`.`experiment_protein_table` SET  `other_protein(s)`=? ,`protein_inference_class`=? ,`sequence_coverage(%)` =?,`observable_coverage(%)`=? ,`confident_ptm_sites`=? ,`number_confident` =?,`other_ptm_sites`=? ,`number_other`=? ,`number_validated_peptides` =?,`number_validated_spectra`=? ,`em_pai` =?,`nsaf` =?,`mw_(kDa)`=? ,`score` =?,`confidence`=? ,`starred`=? , `non_enzymatic_peptides`=? WHERE  `exp_id`=? AND `prot_accession`=?";
//            updateProtStat = conn2.prepareStatement(updateProt, Statement.RETURN_GENERATED_KEYS);
//            updateProtStat.setString(1, pb.getOtherProteins().toUpperCase());
//            updateProtStat.setString(2, pb.getProteinInferenceClass().toUpperCase());
//            updateProtStat.setDouble(3, pb.getSequenceCoverage());
//            updateProtStat.setDouble(4, pb.getObservableCoverage());
//            updateProtStat.setString(5, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
//            updateProtStat.setString(6, pb.getNumberConfident().toString());
//            updateProtStat.setString(7, pb.getOtherPtmSites().toUpperCase());
//            updateProtStat.setString(8, pb.getNumberOfOther().toUpperCase());
//            updateProtStat.setInt(9, pb.getNumberValidatedPeptides());
//            updateProtStat.setInt(10, pb.getNumberValidatedSpectra());
//            updateProtStat.setDouble(11, pb.getEmPai());
//            updateProtStat.setDouble(12, pb.getNsaf());
//            updateProtStat.setDouble(13, pb.getMw_kDa());
//            updateProtStat.setDouble(14, pb.getScore());
//            updateProtStat.setDouble(15, pb.getConfidence());
//            updateProtStat.setString(16, String.valueOf(pb.isStarred()));
//            updateProtStat.setString(17, (String.valueOf(pb.isNonEnzymaticPeptides()).toUpperCase()));
//            updateProtStat.setInt(18, expId);
//            updateProtStat.setString(19, pb.getAccession().toUpperCase());
//            int test = updateProtStat.executeUpdate();
////            test = this.updateProt(conn2, driver, url, dbName, userName, password, pb.getAccession(), pb.getDescription());
//
//            if (test > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//    public synchronized boolean updatePeptide(Connection conn2, PeptideBean pepb) {
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String updatePeptideNumber = "UPDATE  `proteins_peptides_table` SET `protein`=? ,`other_protein(s)`=? ,`peptide_protein(s)`=? ,`other_protein_description(s)`=? ,`peptide_proteins_description(s)`=? ,`aa_before`=? ,`sequence`=? ,"
//                    + "`aa_after`=? ,`peptide_start`=? ,`peptide_end`=? ,`variable_modification`=? ,`location_confidence`=? ,`precursor_charge(s)`=? ,`number_of_validated_spectra`=? ,`score`=? ,`confidence`=?,	,`protein_inference`=?,`sequence_tagged`=?,`enzymatic`=?,`validated`=?,`starred`=? 	`glycopattern_position(s)`=?,`deamidation_and_glycopattern`=?  WHERE `sequence` = ?;";
//            PreparedStatement insertPeptideStat = conn2.prepareStatement(updatePeptideNumber);
//            insertPeptideStat.setString(1, pepb.getProtein().toUpperCase());
//            insertPeptideStat.setString(2, pepb.getOtherProteins().toUpperCase());
//            insertPeptideStat.setString(3, pepb.getPeptideProteins().toUpperCase());
//            insertPeptideStat.setString(4, pepb.getOtherProteinDescriptions().toUpperCase());
//            insertPeptideStat.setString(5, pepb.getPeptideProteinsDescriptions().toUpperCase());
//            insertPeptideStat.setString(6, pepb.getAaBefore().toUpperCase());
//            insertPeptideStat.setString(7, pepb.getSequence().toUpperCase());
//            insertPeptideStat.setString(8, pepb.getAaAfter().toUpperCase());
//            insertPeptideStat.setString(9, pepb.getPeptideStart().toUpperCase());
//            insertPeptideStat.setString(10, pepb.getPeptideEnd().toUpperCase());
//            insertPeptideStat.setString(11, pepb.getVariableModification().toUpperCase());
//            insertPeptideStat.setString(12, pepb.getLocationConfidence().toUpperCase());
//            insertPeptideStat.setString(13, pepb.getPrecursorCharges().toUpperCase());
//            insertPeptideStat.setInt(14, pepb.getNumberOfValidatedSpectra());
//            insertPeptideStat.setDouble(15, pepb.getScore());
//            insertPeptideStat.setDouble(16, pepb.getConfidence());
//
//            insertPeptideStat.setString(17, pepb.getProteinInference().toUpperCase());
//            insertPeptideStat.setString(18, pepb.getSequenceTagged().toUpperCase());
//            insertPeptideStat.setString(19, String.valueOf(pepb.isEnzymatic()).toUpperCase());
//            insertPeptideStat.setDouble(20, pepb.getValidated());
//            insertPeptideStat.setString(21, String.valueOf(pepb.isStarred()).toUpperCase());
//            insertPeptideStat.setString(22, pepb.getGlycopatternPositions());
//
//            if (pepb.isDeamidationAndGlycopattern()) {
//                insertPeptideStat.setString(23, String.valueOf(pepb.isDeamidationAndGlycopattern()).toUpperCase());
//            } else {
//                insertPeptideStat.setString(23, "");
//            }
//
//
//
//            insertPeptideStat.setString(24, pepb.getSequence());
//            insertPeptideStat.executeUpdate();
//            insertPeptideStat.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//
//    }
//    private synchronized ProteinBean getProtein(String accession, int expId) {
//        try {
//            PreparedStatement selectProtExpStat = null;
//            String selectProtExp = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? AND `prot_accession`=?;";
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            selectProtExpStat = conn.prepareStatement(selectProtExp);
//            selectProtExpStat.setInt(1, expId);
//            selectProtExpStat.setString(2, accession);
//            ResultSet rs = selectProtExpStat.executeQuery();
//            ProteinBean temPb = null;
//            while (rs.next()) {
//                temPb = new ProteinBean();
//                temPb.setAccession(accession);
//                temPb.setOtherProteins(rs.getString("other_protein(s)"));
//                temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
//                temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
//                temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
//                temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
//                temPb.setNumberConfident(rs.getString("number_confident"));
//                temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
//                temPb.setNumberOfOther(rs.getString("number_other"));
//                temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
//                temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
//                temPb.setEmPai(rs.getDouble("em_pai"));
//                temPb.setNsaf(rs.getDouble("nsaf"));
//                temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
//                temPb.setScore(rs.getDouble("score"));
//                temPb.setConfidence(rs.getDouble("confidence"));
//                temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
//                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));
//
//                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
//                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
//                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
//                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));
//
//                temPb.setGeneName(rs.getString("gene_name"));
//                temPb.setChromosomeNumber(rs.getString("chromosome_number"));
//                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
//            }
//            rs.close();
//            if (temPb == null) {
//                return null;
//            }
//            PreparedStatement selectProtStat = null;
//            String selectProt = "SELECT   `description` FROM `proteins_table` WHERE `accession`= ? ";
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            selectProtStat = conn.prepareStatement(selectProt);
//            selectProtStat.setString(1, accession.toUpperCase());
//            rs = selectProtStat.executeQuery();
//            while (rs.next()) {
//                temPb.setDescription(rs.getString("description"));
//            }
//            rs.close();
//            return temPb;
//        } catch (Exception e) {
//            System.err.println(e.getLocalizedMessage());
//        }
//        return null;
//    }
    ///new v-2
//    public synchronized List<ProteinBean> searchOtherProteins(String accession, int expId, List<ProteinBean> protList) {
//        PreparedStatement selectProStat = null;
//        String selectPro = "SELECT `prot_accession` FROM `experiment_protein_table` Where `exp_id`=? AND `other_protein(s)` LIKE (?);";
//
//        try {
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            selectProStat = conn.prepareStatement(selectPro);
//            selectProStat.setString(2, "%" + accession + "%");
//            selectProStat.setInt(1, expId);
//            ResultSet rs = selectProStat.executeQuery();
//            List<String> accsList = new ArrayList<String>();
//            while (rs.next()) {
//                accsList.add(rs.getString("prot_accession"));
//
//            }
//            for (String acc : accsList) {
//                ProteinBean pb = this.getProtein(acc, expId);
//                protList.add(pb);
//
//            }
//            rs.close();
//            return protList;
//        } catch (Exception e) {
//            System.err.println(e.getLocalizedMessage());
////            e.printStackTrace();
//            return null;
//        }
//    }
//    public synchronized boolean checkAndUpdateProt(ExperimentBean exp) {
//        boolean test = false;
//        try {
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            for (ProteinBean pb : exp.getProteinList().values()) {
//                test = checkProteinExisting(exp.getExpId(), pb.getAccession());
//                if (test)//existing protein
//                {
//                    test = updateProtein(conn, pb, exp.getExpId());
//                } else {
//                    test = addProtine(conn, pb, exp.getExpId());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            test = false;
//        }
//        return test;
//    }
//    public synchronized boolean updatePeptideFile(ExperimentBean tempExp, ExperimentBean exp) {
//        boolean test = false;
//        try {
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            this.removePepFile(conn, exp);
//            if (tempExp.getPeptidesInclude() == 0)//we need to update peptide file number to 1 
//            {
//                tempExp.setPeptidesInclude(1);
//                tempExp.setPeptidesNumber(exp.getPeptidesNumber());
//                test = updateExperiment(conn, tempExp);
//                for (PeptideBean pepb : exp.getPeptideList().values()) {
//                    insertPeptide(conn, -1, pepb, tempExp.getExpId());
//                }
//            } else {
//
//                tempExp.setPeptidesNumber(exp.getPeptidesNumber());
//                test = updateExperiment(conn, tempExp);
//                int counter = 0;
//                for (PeptideBean pepb : exp.getPeptideList().values()) {
//                    insertPeptide(conn, -1, pepb, tempExp.getExpId());
//                    counter++;
//                    if (counter == 10000) {
//                        conn.close();
//                        Thread.sleep(100);
//                        Class.forName(driver).newInstance();
//                        conn = DriverManager.getConnection(url + dbName, userName, password);
//                        counter = 0;
//                    }
//                }
//
//
//            }
//
//
//        } catch (Exception e) {
//            System.err.println(e.getLocalizedMessage());
////            e.printStackTrace();
//            test = false;
//        }
//        return test;
//    }
//    private boolean removePepFile(Connection conn2, ExperimentBean exp) {
//        PreparedStatement getPepExpStat = null;//done
//        PreparedStatement remPepStat = null;//done
//        PreparedStatement remPepExpStat = null;//done
//        PreparedStatement remPepProExpStat = null;
//
//        try {
//            if (conn2 == null || conn2.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn2 = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            String remPep = "DELETE FROM `" + dbName + "`.`proteins_peptides_table`   WHERE  `peptide_id` =? ";
//            String selectPepList = "SELECT `pep_id` FROM `experiment_peptides_table` where `exp_id` = ?";
//
//            getPepExpStat = conn2.prepareStatement(selectPepList);
//            getPepExpStat.setInt(1, exp.getExpId());
//            ResultSet rs = getPepExpStat.executeQuery();
//            ArrayList<Integer> pepIdList = new ArrayList<Integer>();
//            while (rs.next()) {
//                int pep_id = rs.getInt("pep_id");
//                pepIdList.add(pep_id);
//
//            }
//            rs.close();
//            for (int pepb : pepIdList) {
//                remPepStat = conn2.prepareStatement(remPep);
//                remPepStat.setInt(1, pepb);
//                remPepStat.executeUpdate();
//                remPepStat.clearParameters();
//                remPepStat.close();
//            }
//
//
//            String removePep = "DELETE FROM `experiment_peptides_table`   WHERE `exp_id` = ? ;";
//
//            remPepExpStat = conn2.prepareStatement(removePep);
//            remPepExpStat.setInt(1, exp.getExpId());
//            remPepExpStat.executeUpdate();
//            remPepExpStat.clearParameters();
//            remPepExpStat.close();
//
//            String remPepProExp = "DELETE FROM `" + dbName + "`.`experiment_peptides_proteins_table`   WHERE  `exp_id` =? ";
//            remPepProExpStat = conn2.prepareStatement(remPepProExp);
//            remPepProExpStat.setInt(1, exp.getExpId());
//            remPepProExpStat.executeUpdate();
//            remPepProExpStat.clearParameters();
//            remPepProExpStat.close();
//
//
//
//            return true;
//        } catch (Exception e) {
//            System.err.println(e.getLocalizedMessage());
////            e.printStackTrace();
//            return false;
//        }
//
//    }
}
