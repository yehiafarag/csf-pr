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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.model.beans.QuantProtein;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.beans.QuantDatasetListObject;
import probe.com.model.beans.QuantPeptide;
import probe.com.model.beans.User;

/**
 * @author Yehia Farag abstraction for database
 */
public class DataBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private Connection conn = null;
    private Connection conn_i = null;
    private final String url, dbName, driver, userName, password;
    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    /**
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     *
     */
    public DataBase(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    /**
     * create database tables if not exist
     *
     * @return test boolean (successful creation)
     *
     */
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

                //temp
//               fg String sqoDataBase = "SHOW DATABASES ;";
//                ResultSet rs1 = statement.executeQuery(sqoDataBase);
//               Set<String> datasetnames = new HashSet<String>();
//                while(rs1.next()){
//                    String db = rs1.getString("Database");
//                    datasetnames.add(db);
//                    System.err.println("db is "+db);
//                    
//                }
//                 Statement statement2 = conn_i.createStatement();
//                
//                for(String db:datasetnames)
//                {
//                    if(db.contains("csf")&&!db.equals(dbName))
//                        statement2.executeUpdate("DROP DATABASE "+ db+" ;");
//                
//                }
                conn_i.close();
            } catch (ClassNotFoundException e) {
                System.err.println("at error" + e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                System.err.println("at error" + e.getLocalizedMessage());
            } catch (InstantiationException e) {
                System.err.println("at error" + e.getLocalizedMessage());
            } catch (SQLException e) {
                System.err.println("at error" + e.getLocalizedMessage());
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

                //CREATE TABLE `datasets_table`
                String datasets_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (\n" + "  `exp_id` int(11) NOT NULL auto_increment,\n" + "  `name` varchar(100) NOT NULL,\n" + "  `fractions_number` int(11) NOT NULL default '0',\n" + "  `ready` int(11) NOT NULL default '0',\n" + "  `uploaded_by` varchar(100) NOT NULL,\n" + "  `peptide_file` int(2) NOT NULL default '0',\n"
                        + "  `species` varchar(100) NOT NULL,\n" + "  `sample_type` varchar(100) NOT NULL,\n" + "  `sample_processing` varchar(100) NOT NULL,\n" + "  `instrument_type` varchar(100) NOT NULL,\n" + "  `frag_mode` varchar(100) NOT NULL,\n" + "  `proteins_number` int(11) NOT NULL default '0',\n" + "  `peptides_number` int(11) NOT NULL default '0',\n" + "  `email` varchar(100) NOT NULL,\n" + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',\n"
                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',\n" + "  `exp_type` int(10) NOT NULL default '0',\n" + "  PRIMARY KEY  (`exp_id`)\n" + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;";
                st.executeUpdate(datasets_table);

                //CREATE TABLE dataset_protein_table
                String dataset_protein_table = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (\n"
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
                st.executeUpdate(dataset_protein_table);

                //  CREATE TABLE  `experiment_peptides_table`
                String dataset_peptide_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (  `exp_id` INT NOT NULL DEFAULT  '0',  `pep_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE  ) ENGINE = MYISAM ;";
                st.executeUpdate(dataset_peptide_table);

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
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf1;";
                st.executeUpdate(proteins_peptides_table);
                System.out.println("at context start creating ds ");

                //CREATE TABLE fractions_table
                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (\n"
                        + "  `fraction_id` int(11) NOT NULL,\n"
                        + "  `prot_accession` varchar(500) NOT NULL,\n"
                        + "  `number_peptides` int(11) NOT NULL default '0',\n"
                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                        + "  `number_spectra` int(11) NOT NULL default '0',\n"
                        + "  `average_ precursor_intensity` double default NULL,\n"
                        + "  `exp_id` int(255) NOT NULL default '0',\n"
                        + "  KEY `prot_accession` (`prot_accession`(333)),\n"
                        + "  KEY `fraction_id` (`fraction_id`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf1;";
                st.executeUpdate(fractions_table);

                //CREATE TABLE dataset_peptides_proteins_table
                String dataset_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (  `exp_id` varchar(50) NOT NULL,  `peptide_id` int(50) NOT NULL,  `protein` varchar(70) NOT NULL,  UNIQUE KEY `exp_id` (`exp_id`,`peptide_id`,`protein`),  KEY `peptide_id` (`peptide_id`),  KEY `protein` (`protein`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(dataset_peptides_proteins_table);

                //CREATE TABLEstandard_plot_proteins
                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(standard_plot_proteins);

                //  CREATE TABLE  `quant_prot_table`
                String quant_prot_table = "CREATE TABLE IF NOT EXISTS `quant_prot_table` (\n"
                        + "  `pumed_id` varchar(150) NOT NULL,\n"
                        + "  `uniprot_accession` varchar(150) NOT NULL,\n"
                        + "  `uniprot_protein_name` varchar(700) NOT NULL,\n"
                        + "  `publication_acc_number` varchar(150) default 'Not Available',\n"
                        + "  `publication_protein_name` varchar(700) default 'Not Available',\n"
                        + "  `raw_data_available` varchar(700) default 'Not Available',\n"
                        + "  `type_of_study` varchar(150) default 'Not Available',\n"
                        + "  `sample_type` varchar(150) default 'Not Available',\n"
                        + "  `patient_group_i` varchar(700) default 'Not Available',\n"
                        + "  `patient_sub_group_i` varchar(700) default 'Not Available',\n"
                        + "  `patient_gr_i_comment` varchar(700) default 'Not Available',\n"
                        + "  `patient_group_ii` varchar(700) default 'Not Available',\n"
                        + "  `patient_sub_group_ii` varchar(700) default 'Not Available',\n"
                        + "  `patient_gr_ii_comment` varchar(700) default 'Not Available',\n"
                        + "  `sample_matching` varchar(500) default 'Not Available',\n"
                        + "  `normalization_strategy` varchar(500) default 'Not Available',\n"
                        + "  `technology` varchar(500) default 'Not Available',\n"
                        + "  `analytical_approach` varchar(500) default 'Not Available',\n"
                        + "  `enzyme` varchar(500) default 'Not Available',\n"
                        + "  `shotgun_targeted` varchar(100) default 'Not Available',\n"
                        + "  `quantification_basis` varchar(500) default 'Not Available',\n"
                        + "  `quant_basis_comment` varchar(700) default 'Not Available',\n"
                        + "  `additional_comments` varchar(700) default 'Not Available',\n"
                        + "  `q_peptide_key` varchar(700) default 'Not Available',\n"
                        + "  `peptide_sequance` varchar(700) default 'Not Available',\n"
                        + "  `peptide_modification` varchar(700) default 'Not Available',\n"
                        + "  `modification_comment` varchar(700) default 'Not Available',\n"
                        + "  `string_fc_value` varchar(200) default 'Not Available',\n"
                        + "  `string_p_value` varchar(200) default 'Not Available',\n"
//                        + "  `quantified_proteins_number` int(255) default NULL,\n"
                        + "  `peptideId_number` int(255) default NULL,\n"
                        + "  `quantified_peptides_number` int(255) default NULL,\n"
                        + "  `patients_group_i_number` int(255) default NULL,\n"
                        + "  `patients_group_ii_number` int(255) default NULL,\n"
                        + "  `p_value` double default NULL,\n"
                        + "  `roc_auc` double default NULL,\n"
                        + "  `fc_value` double default NULL,\n"
                        + "  `peptide_prot` varchar(5) NOT NULL default 'False',\n"
                        + "  `index` int(255) NOT NULL auto_increment,\n"
                        + "  PRIMARY KEY  (`index`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(quant_prot_table);

                //CREATE TABLE  `studies_table`
//                String studies_table = "CREATE TABLE IF NOT EXISTS `studies_table` (\n"
//                        + "  `pumed_id` varchar(30) NOT NULL,\n"
//                        + "  `files_num` int(255) NOT NULL default '0',\n"
//                        + "  `identified _proteins_num` int(255) NOT NULL default '0',\n"
//                        + "  `quantified_protein_num` int(255) NOT NULL default '0',\n"
//                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `raw_data_url` varchar(500) NOT NULL default 'Not Available',\n"
//                        + "  `year` int(4) NOT NULL default '0',\n"
//                        + "  `index` int(255) NOT NULL auto_increment,\n"
//                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
//                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
//                        + "  `author` varchar(300) NOT NULL default 'Adam Smith',\n"
//                        + "  PRIMARY KEY  (`index`)\n"
//                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//
//                st.executeUpdate(studies_table);
//CREATE TABLE  Quant_Dataset_table
                String quant_ds_table = "CREATE TABLE IF NOT EXISTS `quant_dataset_table` (\n"
                        + "  `pumed_id` varchar(30) NOT NULL,\n"
                        + "  `files_num` int(255) NOT NULL default '0',\n"
                        + "  `identified _proteins_num` int(255) NOT NULL default '0',\n"
                        + "  `quantified_protein_num` int(255) NOT NULL default '0',\n"
                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `raw_data_url` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `year` int(4) NOT NULL default '0',\n"
                        + "  `index` int(255) NOT NULL auto_increment,\n"
                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
//                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000',\n"
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  PRIMARY KEY  (`index`)\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                System.out.println("at db start quant prot table");
                st.executeUpdate(quant_ds_table);

                String quantitative_proteins_table = "CREATE TABLE IF NOT EXISTS `quantitative_proteins_table` (\n"
                        + "  `index` int(255) NOT NULL auto_increment,\n"
                        + "  `ds_ID` int(255) NOT NULL default '-1',\n"
                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
                        + "  `identified_peptides_number` int(255) NOT NULL default '-1',\n"
                        + "  KEY `index` (`index`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(quantitative_proteins_table);

                String quantitative_peptides_table = "CREATE TABLE IF NOT EXISTS `quantitative_peptides_table` (\n"
                        + "  `index` int(255) NOT NULL auto_increment,\n"
                        + "  `prot_index` int(255) NOT NULL default '-1',\n"
                        + "  `peptide_sequance` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `peptide_modification` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `modification_comment` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `p_value` double NOT NULL default '-1000000000',\n"
                        + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                        + "  `fc_value` double NOT NULL default '-1000000000',\n"
                        + "  KEY `index` (`index`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
                st.executeUpdate(quantitative_peptides_table);
                conn.close();
                st.close();
                System.gc();
            } catch (SQLException s) {
                System.err.println(s.getLocalizedMessage());
                conn.close();

                return false;
            }
            // 
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

//		Storing Data
    /**
     * store protein fractions data
     *
     * @param datasetBean
     * @return test boolean (successful creation)
     */
    public synchronized boolean setProteinFractionFile(IdentificationDataset datasetBean) {
        if (datasetBean.getDatasetId() == -1)//new Experiment
        {
            PreparedStatement insertDatasetStat = null;
            PreparedStatement insertFractDatasetStat = null;
            int datasetId = 0;
            int fractId = 0;
            int test = 0;
            String insertDataset = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready`,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`fractions_number` ,	`email` ,`pblication_link`,`description`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ;";
            String insertFractDataset = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                insertDatasetStat = conn.prepareStatement(insertDataset, Statement.RETURN_GENERATED_KEYS);
                insertDatasetStat.setString(1, datasetBean.getName().toUpperCase());
                insertDatasetStat.setInt(2, 2);
                insertDatasetStat.setString(3, datasetBean.getUploadedByName().toUpperCase());
                insertDatasetStat.setString(4, datasetBean.getSpecies());
                insertDatasetStat.setString(5, datasetBean.getSampleType());
                insertDatasetStat.setString(6, datasetBean.getSampleProcessing());
                insertDatasetStat.setString(7, datasetBean.getInstrumentType());
                insertDatasetStat.setString(8, datasetBean.getFragMode());
                insertDatasetStat.setInt(9, datasetBean.getFractionsNumber());
                insertDatasetStat.setString(10, datasetBean.getEmail().toUpperCase());
                if (datasetBean.getPublicationLink() != null) {
                    insertDatasetStat.setString(11, datasetBean.getPublicationLink().toUpperCase());
                } else {
                    insertDatasetStat.setString(11, "NOT AVAILABLE");
                }
                insertDatasetStat.setString(12, datasetBean.getDescription());
                insertDatasetStat.executeUpdate();
                ResultSet rs = insertDatasetStat.getGeneratedKeys();
                while (rs.next()) {
                    datasetId = rs.getInt(1);
                }
                rs.close();
//                for (FractionBean fb : datasetBean.getFractionsList().values()) {
//                    insertFractDatasetStat = conn.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
//                    insertFractDatasetStat.setInt(1, datasetId);
//                    insertFractDatasetStat.setDouble(2, fb.getMinRange());
//                    insertFractDatasetStat.setDouble(3, fb.getMaxRange());
//                    insertFractDatasetStat.setInt(4, fb.getFractionIndex());
//                    insertFractDatasetStat.executeUpdate();
//                    rs1 = insertFractDatasetStat.getGeneratedKeys();
//                    while (rs1.next()) {
//                        fractId = rs1.getInt(1);
//                    }
//                    rs1.close();
//                    for (IdentificationProteinBean pb : fb.getProteinList().values()) {
//                        test = this.insertProteinFract(conn, fractId, pb);
//                    }
//                }
            } catch (ClassNotFoundException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (InstantiationException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (SQLException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            }
            if (test > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * store one fraction information
     *
     * @param databaseConnection
     * @param fraction
     * @param datasetId
     * @return integer return value
     */
    public synchronized int insertFraction(Connection databaseConnection, FractionBean fraction, int datasetId) {
        String insertFractDataset = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
        int fractId = -1;
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                Class.forName(driver).newInstance();
                databaseConnection = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement insertFractDatasetStat = databaseConnection.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
            insertFractDatasetStat.setInt(1, datasetId);
            insertFractDatasetStat.setDouble(2, 0);
            insertFractDatasetStat.setDouble(3, 0);
            insertFractDatasetStat.setInt(4, fraction.getFractionIndex());
            insertFractDatasetStat.executeUpdate();
            ResultSet rs = insertFractDatasetStat.getGeneratedKeys();
            while (rs.next()) {
                fractId = rs.getInt(1);
            }
            rs.close();
            for (IdentificationProteinBean pb : fraction.getProteinList().values()) {
                this.insertProteinFract(databaseConnection, fractId, pb);
                this.updateDatasetProteinFraction(pb, datasetId);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return 0;
    }

    /**
     * store one fraction information
     *
     * @param databaseConnection
     * @param fractionId
     * @param proteinFraction
     * @return integer return value
     */
    private synchronized int insertProteinFract(Connection databaseConnection, int fractionId, IdentificationProteinBean proteinFraction) {
        int test = -1;
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                Class.forName(driver).newInstance();
                databaseConnection = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtFract = "INSERT INTO  `" + dbName + "`.`fractions_table` (`fraction_id` ,`prot_accession` ,`number_peptides` ,`number_spectra` ,`average_ precursor_intensity`)VALUES (?, ?,  ?,  ?,  ?);";
            PreparedStatement insertProtFracStat = databaseConnection.prepareStatement(insertProtFract, Statement.RETURN_GENERATED_KEYS);
            insertProtFracStat.setInt(1, fractionId);
            insertProtFracStat.setString(2, proteinFraction.getAccession().toUpperCase() + "," + proteinFraction.getOtherProteins().toUpperCase());
            insertProtFracStat.setInt(3, proteinFraction.getNumberOfPeptidePerFraction());
            insertProtFracStat.setInt(4, proteinFraction.getNumberOfSpectraPerFraction());
            insertProtFracStat.setDouble(5, proteinFraction.getAveragePrecursorIntensityPerFraction());

            test = insertProtFracStat.executeUpdate();
            insertProtFracStat.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return -1;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return -1;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return -1;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return -1;
        }
        return test;
    }

    /**
     * update stored fractions number for dataset
     *
     * @param databaseConnection
     * @param datasetId
     * @param fractionsNumber
     * @return integer return value
     */
    public synchronized int updateFractionNumber(Connection databaseConnection, int datasetId, int fractionsNumber) {
        int test = -1;
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                Class.forName(driver).newInstance();
                databaseConnection = DriverManager.getConnection(url + dbName, userName, password);
            }
            String updateFractionNumber = "UPDATE  `experiments_table` SET `fractions_number` = ? WHERE WHERE `exp_id` = ?;";
            PreparedStatement updateFractStat = databaseConnection.prepareStatement(updateFractionNumber);
            updateFractStat.setInt(1, fractionsNumber);
            updateFractStat.setInt(2, datasetId);
            test = updateFractStat.executeUpdate();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        return test;

    }

    /**
     * update stored fractions number for dataset
     *
     * @param databaseConnection
     * @param dataset
     * @return test boolean value
     */
    public synchronized boolean updateDatasetInformation(Connection databaseConnection, IdentificationDataset dataset) {
        PreparedStatement updateDatasetStat = null;
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                Class.forName(driver).newInstance();
                databaseConnection = DriverManager.getConnection(url + dbName, userName, password);
            }
            String updateDataset = "UPDATE `experiments_table`  SET  `name`=? ,`fractions_number`=? ,`ready` =?,`uploaded_by`=?, `peptide_file`=?,`pblication_link`=?, `peptides_number`=? ,`proteins_number`=? ,`fraction_range`=? WHERE  `exp_id`=? ";
            updateDatasetStat = databaseConnection.prepareStatement(updateDataset);

            updateDatasetStat.setString(1, dataset.getName().toUpperCase());
            updateDatasetStat.setInt(2, dataset.getFractionsNumber());
            updateDatasetStat.setInt(3, 2);
            updateDatasetStat.setString(4, dataset.getUploadedByName().toUpperCase());
            updateDatasetStat.setInt(5, dataset.getPeptidesInclude());
            updateDatasetStat.setString(6, dataset.getPublicationLink());
            updateDatasetStat.setInt(7, dataset.getPeptidesNumber());
            updateDatasetStat.setInt(8, dataset.getProteinsNumber());
            updateDatasetStat.setInt(9, 1);
            updateDatasetStat.setInt(10, dataset.getDatasetId());
            int test = updateDatasetStat.executeUpdate();
            updateDatasetStat.close();
            return test > 0;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        }

    }

    /**
     * update stored fractions number for dataset
     *
     * @param databaseConnection
     * @param fractionBean
     * @param datasetId
     * @return test boolean value
     */
    public synchronized boolean updateFractions(Connection databaseConnection, FractionBean fractionBean, int datasetId) {
        List<Integer> fractionIDs = this.getFractionIdsList(datasetId);
        for (int fractId : fractionIDs) {
            try {
                if (databaseConnection == null || databaseConnection.isClosed()) {
                    Class.forName(driver).newInstance();
                    databaseConnection = DriverManager.getConnection(url + dbName, userName, password);
                }
                String updateFraction = "UPDATE  `" + dbName + "`.`fractions_table` SET `number_peptides`=? ,`number_spectra`=? ,`average_ precursor_intensity`=?  WHERE `fraction_id` = ? AND `prot_accession`=?;";

                PreparedStatement updateFractionStat = null;
                for (IdentificationProteinBean fpb : fractionBean.getProteinList().values()) {
                    boolean test = this.checkDatasetFractionProtein(fractId, fpb.getAccession());
                    if (test) {
                        updateFractionStat = databaseConnection.prepareStatement(updateFraction);
                        updateFractionStat.setInt(1, fpb.getNumberOfPeptidePerFraction());
                        updateFractionStat.setInt(2, fpb.getNumberOfSpectraPerFraction());
                        updateFractionStat.setDouble(3, fpb.getAveragePrecursorIntensityPerFraction());
                        updateFractionStat.setInt(4, fractionBean.getFractionId());
                        updateFractionStat.setString(5, fpb.getAccession().toUpperCase());
                        updateFractionStat.executeUpdate();
                    } else {
                        this.insertFraction(databaseConnection, fractionBean, datasetId);
                    }

                }

            } catch (ClassNotFoundException e) {
                System.err.println("at error" + e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                System.err.println("at error" + e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (InstantiationException e) {
                System.err.println("at error" + e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (SQLException e) {
                System.err.println("at error" + e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            }

        }

        return false;

    }

    /**
     * check the availability of fraction for a protein in dataset
     *
     * @param fractionId
     * @param accession
     * @return test boolean value
     */
    private synchronized boolean checkDatasetFractionProtein(int fractionId, String accession) {
        PreparedStatement selectFractStat = null;

        String selectFractProt = "SELECT `number_spectra` FROM `fractions_table` WHERE `fraction_id`=? AND `prot_accession`=?;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractStat = conn.prepareStatement(selectFractProt);
            selectFractStat.setInt(1, fractionId);
            selectFractStat.setString(2, accession);
            ResultSet rs = selectFractStat.executeQuery();
            while (rs.next()) {

                return true;
            }

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        return false;
    }

    /**
     * check the availability of a protein in dataset
     *
     * @param datasetId
     * @param accession
     * @return test boolean value
     */
    public synchronized boolean checkProteinExisting(int datasetId, String accession)//check if the protein exist so we use update 
    {
        PreparedStatement selectDatasetStat = null;
        boolean test = false;
        String selectDatasetProt = "SELECT `exp_id`,`prot_accession` FROM `experiment_protein_table` where `exp_id`=? and `prot_accession` = ?";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDatasetProt);
            selectDatasetStat.setInt(1, datasetId);
            selectDatasetStat.setString(2, accession);
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {
                test = true;
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        return test;
    }

    /**
     * check the availability of a dataset
     *
     * @param datasetId
     * @return dataset if available and null if not
     */
    public synchronized IdentificationDataset readyToRetrieveDataset(int datasetId) {
        PreparedStatement selectDatasetStat = null;

        String selectDatasetProt = "SELECT * FROM `experiments_table` WHERE `exp_id` = ?";

        IdentificationDataset dataset = new IdentificationDataset();

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDatasetProt);
            selectDatasetStat.setInt(1, datasetId);
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {

                int fractionsNumber = rs.getInt("fractions_number");
                dataset.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                dataset.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                dataset.setName(name);
                String species = rs.getString("species");
                dataset.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                dataset.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                dataset.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                dataset.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                dataset.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                dataset.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                dataset.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                dataset.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                dataset.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                dataset.setPeptidesNumber(peptidesNumber);
                dataset.setDatasetId(datasetId);

            }
            rs.close();
            System.gc();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        }
        return dataset;

    }

    /**
     * check the fraction number of a dataset
     *
     * @param datasetId
     * @return dataset if available and null if not
     */
    public synchronized int getFractionNumber(int datasetId) {
        PreparedStatement selectDatasetStat = null;
        int fractionNumber = 0;
        String selectDataset = "SELECT `fractions_number` FROM `experiments_table` WHERE `exp_id` = ?;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDataset);
            selectDatasetStat.setInt(1, datasetId);
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {
                fractionNumber = rs.getInt("fractions_number");
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return fractionNumber;
    }

    /**
     * check the peptide availability for special peptide sequence a dataset
     *
     * @param sequence
     * @return test boolean
     */
    public synchronized boolean checkPeptideAvailability(String sequence) {
        PreparedStatement selectDatasetStat = null;
        boolean test = false;
        String selectDatasetProt = "SELECT `sequence` FROM `proteins_peptides_table` WHERE `sequence`=?";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDatasetProt);
            selectDatasetStat.setString(1, sequence);
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {
                test = true;
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return test;
    }

    // RETRIVEING DATA
    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public synchronized Map<Integer, IdentificationDataset> getDatasets()//get experiments list
    {
        PreparedStatement selectDatasetListStat = null;
        Map<Integer, IdentificationDataset> datasetList = new HashMap<Integer, IdentificationDataset>();
        Map<Integer, IdentificationDataset> tempDatasetList = new HashMap<Integer, IdentificationDataset>();
        String selectselectDatasetList = "SELECT * FROM `experiments_table` ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetListStat = conn.prepareStatement(selectselectDatasetList);
            ResultSet rs = selectDatasetListStat.executeQuery();
            while (rs.next()) {
                IdentificationDataset dataset = new IdentificationDataset();
                int fractionsNumber = rs.getInt("fractions_number");
                dataset.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                dataset.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                dataset.setName(name);
                String species = rs.getString("species");
                dataset.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                dataset.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                dataset.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                dataset.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                dataset.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                dataset.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                dataset.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                dataset.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                dataset.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                dataset.setPeptidesNumber(peptidesNumber);
                int datasetId = rs.getInt("exp_id");
                dataset.setDatasetId(datasetId);
                String desc = rs.getString("description");
                dataset.setDescription(desc);
                int datasetType = rs.getInt("exp_type");
                dataset.setDatasetType(datasetType);
                dataset.setNumberValidProt(rs.getInt("valid_prot"));
                datasetList.put(dataset.getDatasetId(), dataset);
            }
            rs.close();
            for (int datasetId : datasetList.keySet()) {
                IdentificationDataset dataset = datasetList.get(datasetId);
                List<Integer> fractionIds = this.getFractionIdsList(datasetId);
                dataset.setFractionIds(fractionIds);
                tempDatasetList.put(datasetId, dataset);

            }
            datasetList.clear();
            datasetList.putAll(tempDatasetList);
            tempDatasetList.clear();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        }
        System.gc();
//        singleuseUpdateDb();
//        initPublications();
        return datasetList;

    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public synchronized IdentificationDataset getStoredDataset(int datasetId) {
        IdentificationDataset dataset = new IdentificationDataset();
        dataset.setDatasetId(datasetId);
        dataset = this.getDatasetDetails(dataset);
//        dataset.setFractionsList(this.getFractionsList(dataset.getDatasetId()));
//        dataset.setProteinList(this.getDatasetProteinsList(datasetId));	   	//get protein details	
//        dataset.setPeptideList(this.getDatasetPeptidesList(datasetId));
        System.gc();
        return dataset;
    }

    /**
     * get fractions Ids list for a dataset
     *
     * @param datasetId
     * @return list of fraction Id's list
     */
    private synchronized List<Integer> getFractionIdsList(int datasetId) {
        PreparedStatement selectDatasetFractionStat = null;
        String selectDatasetFraction = "SELECT `fraction_id` FROM `experiment_fractions_table` WHERE `exp_id`=?;";
        List<Integer> fractionList = new ArrayList<Integer>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetFractionStat = conn.prepareStatement(selectDatasetFraction);
            selectDatasetFractionStat.setInt(1, datasetId);
            ResultSet rs = selectDatasetFractionStat.executeQuery();
            while (rs.next()) {
                fractionList.add(rs.getInt("fraction_id"));
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        return fractionList;
    }

    /**
     * get dataset details
     *
     * @param dataset
     * @return list of fraction Id's list
     */
    private synchronized IdentificationDataset getDatasetDetails(IdentificationDataset dataset) {
        PreparedStatement selectDatasetStat = null;
        String selectDataset = "SELECT * FROM `experiments_table` WHERE `exp_id`=? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDataset);
            selectDatasetStat.setInt(1, dataset.getDatasetId());
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {
                int fractionsNumber = rs.getInt("fractions_number");
                dataset.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                dataset.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                dataset.setName(name);
                String species = rs.getString("species");
                dataset.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                dataset.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                dataset.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                dataset.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                dataset.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                dataset.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                dataset.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                dataset.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                dataset.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                dataset.setPeptidesNumber(peptidesNumber);
                String dec = rs.getString("description");
                dataset.setDescription(dec);
                int DatasetType = rs.getInt("exp_type");
                dataset.setDatasetType(DatasetType);
                dataset.setNumberValidProt(rs.getInt("valid_prot"));
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        return dataset;

    }

    /**
     * get dataset fractions list
     *
     * @param datasetId
     * @return fractions list for the selected dataset
     */
    public synchronized Map<Integer, IdentificationProteinBean> getProtGelFractionsList(int datasetId, String accession, String otherAccession) {
//        Map<Integer, FractionBean> fractionsList = new TreeMap<Integer, FractionBean>();
        try {

            //get fractions id list
//            PreparedStatement selectFractsListStat = null;
//            double minRange = 0.0;
//            double maxRange = 0.0;
//            String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range`,`index` FROM `experiment_fractions_table` where `exp_id` = ? ORDER BY `fraction_id`";
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            selectFractsListStat = conn.prepareStatement(selectFractList);
//            selectFractsListStat.setInt(1, datasetId);
//            ResultSet rs1 = selectFractsListStat.executeQuery();
//            ArrayList<FractionBean> fractionIdList = new ArrayList<FractionBean>();
//            FractionBean fb = null;
//            while (rs1.next()) {
//                fb = new FractionBean();
//                int fraction_id = rs1.getInt("fraction_id");
//                fb.setFractionId(fraction_id);
////                minRange = rs1.getDouble("min_range");
////                fb.setMinRange(minRange);
////                maxRange = rs1.getDouble("max_range");
////                fb.setMaxRange(maxRange);
//                int index = rs1.getInt("index");
//                fb.setFractionIndex(index);
//                fractionIdList.add(fb);
//
//            }
//            rs1.close();
            //get fractions 
            PreparedStatement selectFractsStat = null;
            String selectFract = "SELECT * FROM `fractions_table` where `exp_id` = ? AND `prot_accession` = ?  ORDER BY `fraction_id`";

//            for (FractionBean fb2 : fractionIdList) {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractsStat = conn.prepareStatement(selectFract);
            selectFractsStat.setInt(1, datasetId);
            selectFractsStat.setString(2, accession.toUpperCase() + "," + otherAccession.toUpperCase());
            ResultSet rs = selectFractsStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinList = new HashMap<Integer, IdentificationProteinBean>();
            otherSymbols.setGroupingSeparator('.');
            df = new DecimalFormat("#.##", otherSymbols);

            int x = 0;
            while (rs.next()) {
                IdentificationProteinBean pb = new IdentificationProteinBean();//fraction_id		  			
                pb.setAccession(rs.getString("prot_accession"));
                pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
                pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
                pb.setAveragePrecursorIntensityPerFraction(Double.valueOf(df.format(rs.getDouble("average_ precursor_intensity"))));
                pb.setFrcationId(rs.getInt("fraction_id"));
                proteinList.put(x++, pb);
            }

//                fb2.setProteinList(proteinList);
//                fractionsList.put(fb2.getFractionId(), fb2);
            rs.close();

//            }
            conn.close();
            return proteinList;

//            for(IdentificationProteinBean protBean : proteinList.values()){
//                if(fractionsList.containsKey(protBean.getFrcationId()))
//                {
//                    fractionsList.get(protBean.getFrcationId()).getProteinList().put(protBean.getAccession(), protBean);
//                }else{
//                    FractionBean fb = new FractionBean();
//                    fb.setFractionId(protBean.getFrcationId());
//                    fb.getProteinList().put(protBean.getAccession(), protBean);
//                    fractionsList.put(fb.getFractionId(), fb);
//                
//                }
//            
//            
//            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;

    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
    @SuppressWarnings("SleepWhileInLoop")
    public synchronized Map<Integer, PeptideBean> getDatasetPeptidesList(int datasetId, boolean valid) {
        Map<Integer, PeptideBean> peptidesList = null;
        try {
            //get fractions id list
            PreparedStatement selectPeptideListStat = null;
            String selectPeptideList = "";
            if (valid) {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ? AND `validated` =1;";
            } else {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ?;";
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptideListStat = conn.prepareStatement(selectPeptideList);
            selectPeptideListStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            peptidesList = fillPeptideInformation(rs);
//            ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
//            while (rs1.next()) {
//                PeptideBean pb = new PeptideBean();
//                int peptideId = rs1.getInt("pep_id");
////                peptideIdList.add(peptideId);
//
//            }
//            rs1.close();

            //get peptides 
//            PreparedStatement selectPeptidesStat = null;
//            String selectQuantPeptides = "SELECT * FROM `proteins_peptides_table` WHERE `peptide_id` = ?;";
//            int counter = 0;
//            for (int pepId : peptideIdList) {
//
//                PeptideBean pepb = new PeptideBean();
//                pepb.setPeptideId(pepId);
//                if (conn == null || conn.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                selectPeptidesStat = conn.prepareStatement(selectQuantPeptides);
//                selectPeptidesStat.setInt(1, pepId);
//                rs1 = selectPeptidesStat.executeQuery();
//
//                while (rs1.next()) {
//                    pepb.setAaAfter(rs1.getString("aa_after"));
//                    pepb.setAaBefore(rs1.getString("aa_before"));
//                    pepb.setConfidence(rs1.getDouble("confidence"));
//                    pepb.setLocationConfidence(rs1.getString("location_confidence"));
//                    pepb.setNumberOfValidatedSpectra(rs1.getInt("number_of_validated_spectra"));
//                    pepb.setOtherProteinDescriptions(rs1.getString("other_protein_description(s)"));
//                    pepb.setOtherProteins(rs1.getString("other_protein(s)"));
//                    pepb.setPeptideEnd(rs1.getString("peptide_end"));
//                    pepb.setPeptideProteins((rs1.getString("peptide_protein(s)")));
//                    pepb.setPeptideProteinsDescriptions(rs1.getString("peptide_proteins_description(s)"));
//                    pepb.setPeptideStart(rs1.getString("peptide_start"));
//                    pepb.setPrecursorCharges(rs1.getString("precursor_charge(s)"));
//                    pepb.setProtein(rs1.getString("protein"));
//                    pepb.setScore(rs1.getDouble("score"));
//                    pepb.setSequence(rs1.getString("sequence"));
//                    pepb.setVariableModification(rs1.getString("variable_modification"));
//                    pepb.setFixedModification(rs1.getString("fixed_modification"));
//                    pepb.setPeptideId(pepId);
//                    pepb.setProteinInference(rs1.getString("protein_inference"));
//                    pepb.setSequenceTagged(rs1.getString("sequence_tagged"));
//                    pepb.setEnzymatic(Boolean.valueOf(rs1.getString("enzymatic")));
//                    pepb.setValidated(rs1.getDouble("validated"));
//                    pepb.setStarred(Boolean.valueOf(rs1.getString("starred")));
//
//                    pepb.setGlycopatternPositions(rs1.getString("glycopattern_position(s)"));
//                    String str = rs1.getString("deamidation_and_glycopattern");
//                    if (str != null && !str.equals("")) {
//                        pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
//                    }
//                    pepb.setLikelyNotGlycosite(Boolean.valueOf(rs1.getString("likelyNotGlycosite")));
//
//                    peptidesList.put(pepb.getPeptideId(), pepb);
//
//                }
//                rs1.close();
//                counter++;
//                if (counter == 10000) {
//                    conn.close();
//                    Thread.sleep(100);
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                    counter = 0;
//                }
//
//            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

//        } catch (InterruptedException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return peptidesList;
    }

    public int getAllDatasetPeptidesNumber(int datasetId, boolean validated) {

        try {
            //get fractions id list
            PreparedStatement selectPeptideListStat = null;
            String selectPeptideList = "";
            if (validated) {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ? AND `validated` =1;";
            } else {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ?;";
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptideListStat = conn.prepareStatement(selectPeptideList);
            selectPeptideListStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            return rs.getRow();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

//        } catch (InterruptedException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return 0;
    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public synchronized Map<String, IdentificationProteinBean> getDatasetProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinDatasetList = new HashMap<String, IdentificationProteinBean>();
        try {
            PreparedStatement selectProtDatasetStat = null;
            String selectProtDataset = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProtDatasetStat = conn.prepareStatement(selectProtDataset);
            selectProtDatasetStat.setInt(1, datasetId);
            ResultSet rs = selectProtDatasetStat.executeQuery();
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
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
                    proteinDatasetList.put(temPb.getAccession(), temPb);
                } else {
                    proteinDatasetList.put(temPb.getAccession() + "," + temPb.getOtherProteins(), temPb);
                }
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return proteinDatasetList;
    }

    //REMOVE DATA
    /**
     * remove dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public synchronized boolean removeDataset(int datasetId) {

        PreparedStatement remDatasetStat = null;//done
        PreparedStatement getFractDatasetStat = null;//done
        PreparedStatement remFractStat = null;//done
        PreparedStatement remFractDatasetStat = null;//done
        PreparedStatement getPepDatasetStat = null;//done
        PreparedStatement remPepDatasetStat = null;//done
        PreparedStatement remPeptStat = null;//done
        PreparedStatement remProtStat = null;//done

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            this.removeStandarPlot(datasetId);
            String remDataset = "DELETE FROM `experiments_table`  WHERE  `exp_id`=? ";

            String remFract = "DELETE FROM `" + dbName + "`.`fractions_table`   WHERE  `fraction_id` =? ";

            remDatasetStat = conn.prepareStatement(remDataset);
            remDatasetStat.setInt(1, datasetId);
            remDatasetStat.executeUpdate();

            String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getPepDatasetStat = conn.prepareStatement(selectPeptideList);
            getPepDatasetStat.setInt(1, datasetId);
            ResultSet rs = getPepDatasetStat.executeQuery();
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
            remPepDatasetStat = conn.prepareStatement(removePeptide);
            remPepDatasetStat.setInt(1, datasetId);
            remPepDatasetStat.executeUpdate();

            String remDatasetPro = "DELETE FROM `" + dbName + "`.`experiment_protein_table`  WHERE  `exp_id`=? ";
            remProtStat = conn.prepareStatement(remDatasetPro);
            remProtStat.setInt(1, datasetId);
            remProtStat.executeUpdate();

            String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getFractDatasetStat = conn.prepareStatement(selectFractList);
            getFractDatasetStat.setInt(1, datasetId);
            rs = getFractDatasetStat.executeQuery();
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
            remFractDatasetStat = conn.prepareStatement(removeFraction);
            remFractDatasetStat.setInt(1, datasetId);
            remFractDatasetStat.executeUpdate();

            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return false;
        }
    }

    /**
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";

        String[] queryWordsArr = accession.split(",");
        StringBuilder sb = new StringBuilder();

        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }

        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`prot_key` LIKE(?) ");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id` = ? AND  " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id` = ? AND  " + (sb.toString());
        }

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            selectProStat.setInt(index++, datasetId);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }

            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();

            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * search for proteins by accession keywords
     *
     * @param searchSet set of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByAccession(Set<String> searchSet, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";

        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`prot_key` LIKE(?)");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where  " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where  " + (sb.toString());
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();

            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * search for quant proteins by keywords
     *
     *
     * @return Quant Proteins Searching List
     */
    public synchronized List<QuantProtein> searchQuantificationProteins(Query query) {

        StringBuilder sb = new StringBuilder();
        QueryConstractorHandler qhandler = new QueryConstractorHandler();

        PreparedStatement selectProStat = null;
        String selectPro = "";
        //main filters  
        if (query.getSearchKeyWords() != null && !query.getSearchKeyWords().equalsIgnoreCase("")) {
            String[] queryWordsArr = query.getSearchKeyWords().split(",");
            HashSet<String> searchSet = new HashSet<String>();
            for (String str : queryWordsArr) {
                searchSet.add(str.trim());
            }
            if (query.getSearchBy().equalsIgnoreCase("Protein Accession")) {

                System.out.println("prot acc " + query.getSearchBy());
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`uniprot_accession` = ? OR `publication_acc_number` = ?");
                    qhandler.addQueryParam("String", str);
                    qhandler.addQueryParam("String", str);
                    x++;

                }

            } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                System.out.println("prot name");
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`uniprot_protein_name` LIKE(?) OR `publication_protein_name` LIKE(?)");
                    qhandler.addQueryParam("String", "%" + str + "%");
                    qhandler.addQueryParam("String", "%" + str + "%");
                    x++;

                }

            } else {  //peptide sequance
                System.out.println("pep seq");
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`peptide_sequance` = ?");
                    qhandler.addQueryParam("String", str);
                    x++;

                }

            }
        }

        selectPro = "SELECT * FROM `quant_prot_table` Where " + (sb.toString());

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat = qhandler.initStatment(selectProStat);

//            int index = 1;
//            selectProStat.setInt(index++, datasetId);
//            for(String str:searchSet)
//                selectProStat.setString(index++, "%" + str + "%");
//            
//            if (validatedOnly) {
//                selectProStat.setString(index, "TRUE");
//            }
            ResultSet rs = selectProStat.executeQuery();
//           
            List<QuantProtein> quantProtResultList = fillQuantProtData(rs);
            System.gc();
            return quantProtResultList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

            return null;
        }

    }

   

    /**
     * get peptides list for giving ids
     *
     * @param peptideIds peptides IDs
     * @return peptides list
     */
    public synchronized Map<Integer, PeptideBean> getPeptidesList(String accession, String otherAcc, int datasetId) {

        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        sb.append("`protein` = ?");
        if (otherAcc != null && !otherAcc.equalsIgnoreCase("")) {
            sb.append(" AND ");
            sb.append("`other_protein(s)` = ?");
        }

//        Map<Integer, PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();
        try {

            PreparedStatement selectPeptidesStat = null;
            String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE  `exp_id` = ? AND " + (sb.toString()) + ";";

//            for (int pepId : peptideIds) {
//                pepb.setPeptideId(pepId);
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptidesStat = conn.prepareStatement(selectPeptide);
            selectPeptidesStat.setInt(1, datasetId);
            selectPeptidesStat.setString(2, accession);
            if (otherAcc != null && !otherAcc.equalsIgnoreCase("")) {
                selectPeptidesStat.setString(3, otherAcc);
            }
            rs = selectPeptidesStat.executeQuery();

            return fillPeptideInformation(rs);
//                while (rs1.next()) {      
//                    PeptideBean pepb = new PeptideBean();
//                    pepb.setAaAfter(rs1.getString("aa_after"));
//                    pepb.setAaBefore(rs1.getString("aa_before"));
//                    pepb.setConfidence(rs1.getDouble("confidence"));
//                    pepb.setLocationConfidence(rs1.getString("location_confidence"));
//                    pepb.setNumberOfValidatedSpectra(rs1.getInt("number_of_validated_spectra"));
//                    pepb.setOtherProteinDescriptions(rs1.getString("other_protein_description(s)"));
//                    pepb.setOtherProteins(rs1.getString("other_protein(s)"));
//                    pepb.setPeptideEnd(rs1.getString("peptide_end"));
//                    pepb.setPeptideProteins((rs1.getString("peptide_protein(s)")));
//                    pepb.setPeptideProteinsDescriptions(rs1.getString("peptide_proteins_description(s)"));
//                    pepb.setPeptideStart(rs1.getString("peptide_start"));
//                    pepb.setPrecursorCharges(rs1.getString("precursor_charge(s)"));
//                    pepb.setProtein(rs1.getString("protein"));
//                    pepb.setScore(rs1.getDouble("score"));
//                    pepb.setSequence(rs1.getString("sequence"));
//                    pepb.setFixedModification(rs1.getString("fixed_modification"));
//                    pepb.setVariableModification(rs1.getString("variable_modification"));
//                    pepb.setProteinInference(rs1.getString("protein_inference"));
//                    pepb.setSequenceTagged(rs1.getString("sequence_tagged"));
//                    pepb.setEnzymatic(Boolean.valueOf(rs1.getString("enzymatic")));
//                    pepb.setValidated(rs1.getDouble("validated"));
//                    pepb.setStarred(Boolean.valueOf(rs1.getString("starred")));
//                    pepb.setPeptideId(rs1.getInt("peptide_id"));
//                    pepb.setGlycopatternPositions(rs1.getString("glycopattern_position(s)"));
//                    String str = rs1.getString("deamidation_and_glycopattern");
//                    if (str != null && !str.equals("")) {
//                        pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
//                    }
//
//                    pepb.setLikelyNotGlycosite(Boolean.valueOf(rs1.getString("likelyNotGlycosite")));
//
//                    peptidesList.put(pepb.getPeptideId(), pepb);
//
//                }
//                rs1.close();

//            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return null;
    }

    /**
     * get proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public synchronized Map<Integer, FractionBean> getProteinFractionList(String accession, int datasetId) {
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
            selectFractsListStat.setInt(1, datasetId);
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
            String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ? AND UPPER(`prot_accession`) LIKE UPPER(?) ";

            for (int fractId : fractionIdList) {

                FractionBean fb = new FractionBean();
//                fb.setMinRange(minRange);
//                fb.setMaxRange(maxRange);
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
                Map<String, IdentificationProteinBean> proteinList = new HashMap<String, IdentificationProteinBean>();
                otherSymbols.setGroupingSeparator('.');
                df = new DecimalFormat("#.##", otherSymbols);
                while (rs.next()) {
                    IdentificationProteinBean pb = new IdentificationProteinBean();//fraction_id		  			
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

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return fractionsList;

    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";
        String[] queryWordsArr = protSearchKeyword.split(",");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`description` LIKE(?)");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `exp_id`=? AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `exp_id`=? ";
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
//            selectProStat.setInt(index++, datasetId);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
//            selectProStat.setString(1, "%" + protSearchKeyword + "%");
            selectProStat.setInt(index++, datasetId);
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";
        String[] queryWordsArr = protSearchKeyword.split("\n");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
            System.out.println("key is " + str);
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`description` LIKE(?)");

        }
        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString());
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int index = 1;
            selectProStat = conn.prepareStatement(selectPro);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }

//            selectProStat.setString(1, "%" + protSearchKeyword + "%");
//            selectProStat.setInt(2, datasetId);
//            if (validatedOnly) {
//                selectProStat.setString(3, "TRUE");
//            }
            ResultSet rs = selectProStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
//    public synchronized Map<Integer, IdentificationProteinBean> searchProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
//        PreparedStatement selectProStat = null;
//        String selectPro = "";
//
//        if (validatedOnly) {
//            selectPro = "SELECT * FROM `experiment_protein_table` WHERE `description` LIKE (?)  AND `valid`=?;";
//        } else {
//            selectPro = "SELECT * FROM `experiment_protein_table` WHERE `description` LIKE (?) ";
//        }
//        try {
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            selectProStat = conn.prepareStatement(selectPro);
//            selectProStat.setString(1, "%" + protSearchKeyword + "%");
//            if (validatedOnly) {
//                selectProStat.setString(2, "TRUE");
//            }
//            ResultSet rs1 = selectProStat.executeQuery();
//          Map<Integer, IdentificationProteinBean> proteinsList=fillProteinInformation(rs1);
//            System.gc();
//            return proteinsList;
//        } catch (ClassNotFoundException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
//
//        } catch (IllegalAccessException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
//
//        } catch (InstantiationException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
//
//        } catch (SQLException e) {
//            System.err.println("at error"+e.getLocalizedMessage());
//
//        }
//
//        System.gc();
//        return null;
//    }
    private Map<Integer, PeptideBean> fillPeptideInformation(ResultSet rs) {
        Map<Integer, PeptideBean> peptidesList = new HashMap<Integer, PeptideBean>();
        try {
            while (rs.next()) {
                PeptideBean pepb = new PeptideBean();
                pepb.setProtein(rs.getString("protein"));
                pepb.setOtherProteins(rs.getString("other_protein(s)"));
                pepb.setPeptideProteins((rs.getString("peptide_protein(s)")));
                pepb.setOtherProteinDescriptions(rs.getString("other_protein_description(s)"));
                pepb.setPeptideProteinsDescriptions(rs.getString("peptide_proteins_description(s)"));
                pepb.setAaBefore(rs.getString("aa_before"));
                pepb.setAaAfter(rs.getString("aa_after"));
                pepb.setSequence(rs.getString("sequence"));

                pepb.setPeptideEnd(rs.getString("peptide_end"));
                pepb.setPeptideStart(rs.getString("peptide_start"));

                pepb.setVariableModification(rs.getString("variable_modification"));

                pepb.setLocationConfidence(rs.getString("location_confidence"));
                pepb.setPrecursorCharges(rs.getString("precursor_charge(s)"));
                pepb.setNumberOfValidatedSpectra(rs.getInt("number_of_validated_spectra"));
                pepb.setScore(rs.getDouble("score"));
                pepb.setConfidence(rs.getDouble("confidence"));
                pepb.setPeptideId(rs.getInt("peptide_id"));

                pepb.setFixedModification(rs.getString("fixed_modification"));

                pepb.setProteinInference(rs.getString("protein_inference"));
                pepb.setSequenceTagged(rs.getString("sequence_tagged"));
                pepb.setEnzymatic(Boolean.valueOf(rs.getString("enzymatic")));
                pepb.setValidated(rs.getDouble("validated"));
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

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return peptidesList;
    }

    private List<QuantProtein> fillQuantProtData(ResultSet rs) {

        List<QuantProtein> quantProtResultList = new ArrayList<QuantProtein>();

        try {
            while (rs.next()) {

                QuantProtein qpb = new QuantProtein();
                qpb.setPumedID(rs.getString("pumed_id"));
                qpb.setUniprotAccession(rs.getString("uniprot_accession"));
                qpb.setUniprotProteinName(rs.getString("uniprot_protein_name"));
                qpb.setPublicationAccNumber(rs.getString("publication_acc_number"));
                qpb.setPublicationProteinName(rs.getString("publication_protein_name"));
                qpb.setRawDataAvailable(rs.getString("raw_data_available"));
                qpb.setTypeOfStudy(rs.getString("type_of_study"));
                qpb.setSampleType(rs.getString("sample_type"));
                qpb.setPatientGroupI(rs.getString("patient_group_i"));
                qpb.setPatientSubGroupI(rs.getString("patient_sub_group_i"));
                qpb.setPatientGrIComment(rs.getString("patient_gr_i_comment"));

                qpb.setPatientGroupII(rs.getString("patient_group_ii"));
                qpb.setPatientSubGroupII(rs.getString("patient_sub_group_ii"));
                qpb.setPatientGrIIComment(rs.getString("patient_gr_ii_comment"));
                qpb.setSampleMatching(rs.getString("sample_matching"));
                qpb.setNormalizationStrategy(rs.getString("normalization_strategy"));
                qpb.setTechnology(rs.getString("technology"));
                qpb.setAnalyticalApproach(rs.getString("analytical_approach"));
                qpb.setEnzyme(rs.getString("enzyme"));
                qpb.setShotgunOrTargetedQquant(rs.getString("shotgun_targeted"));
                qpb.setQuantificationBasis(rs.getString("quantification_basis"));
                qpb.setQuantBasisComment(rs.getString("quant_basis_comment"));
                qpb.setAdditionalComments(rs.getString("additional_comments"));
                qpb.setqPeptideKey(rs.getString("q_peptide_key"));
                qpb.setPeptideSequance(rs.getString("peptide_sequance"));
                qpb.setPeptideModification(rs.getString("peptide_modification"));
                qpb.setModificationComment(rs.getString("modification_comment"));
                qpb.setStringFCValue(rs.getString("string_fc_value"));
                qpb.setStringPValue(rs.getString("string_p_value"));
                qpb.setPeptideProt(Boolean.valueOf(rs.getString("peptide_prot")));
                qpb.setPatientsGroupINumber(rs.getInt("patients_group_i_number"));
                qpb.setPatientsGroupIINumber(rs.getInt("patients_group_ii_number"));
                qpb.setQuantifiedProteinsNumber(rs.getInt("quantified_proteins_number"));
                qpb.setPeptideIdNumb(rs.getInt("peptideId_number"));
                qpb.setQuantifiedPeptidesNumber(rs.getInt("quantified_peptides_number"));
                qpb.setFcPatientGroupIonPatientGroupII(rs.getDouble("fc_value"));
                qpb.setRocAuc(rs.getDouble("roc_auc"));
                qpb.setpValue(rs.getDouble("p_value"));

                quantProtResultList.add(qpb);

            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return quantProtResultList;

    }

    private Map<Integer, IdentificationProteinBean> fillProteinInformation(ResultSet rs) {
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<Integer, IdentificationProteinBean>();
        try {
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setDatasetId(rs.getInt("exp_id"));
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
        } catch (SQLException sqlExcp) {
            System.err.println(sqlExcp.getLocalizedMessage());
        }
        return proteinsList;

    }

    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        PreparedStatement selectPepIdStat = null;
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<Integer, IdentificationProteinBean>();
        List<Integer> pepIdList = new ArrayList<Integer>();
        String selectPepId = "SELECT `peptide_id`  FROM `proteins_peptides_table` WHERE `exp_id` = ? AND `sequence` = ? ;";
        Set<String> accessionList = new HashSet<String>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPepIdStat = conn.prepareStatement(selectPepId);
            selectPepIdStat.setInt(1, datasetId);
            selectPepIdStat.setString(2, peptideSequenceKeyword);
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
                selectProStat.setInt(1, datasetId);
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
                            Map<Integer, IdentificationProteinBean> tempProteinsList = this.searchProteinByAccession(str.trim(), datasetId, validatedOnly);
                            if (tempProteinsList != null) {
                                proteinsList.putAll(tempProteinsList);
                            }
                        }
                    }
                }

            }
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> SearchProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword, boolean validatedOnly) {
//        PreparedStatement selectProStat = null;
        PreparedStatement selectPepIdStat = null;
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<Integer, IdentificationProteinBean>();
        Map<Integer, IdentificationProteinBean> filteredProteinsList = new HashMap<Integer, IdentificationProteinBean>();

        String[] queryWordsArr = peptideSequenceKeyword.split(",");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            if (str.trim().equalsIgnoreCase("")) {
                searchSet.add(str.trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append(" `sequence` = ? ");

        }

        Set<String> protAccessionQuerySet = new HashSet<String>();
        Set<Integer> expIds = new HashSet<Integer>();
        String selectProtAccession = "SELECT  `protein` ,  `other_protein(s)` ,  `peptide_protein(s)` , `exp_id` \n"
                + "FROM  `proteins_peptides_table`  WHERE " + (sb.toString()) + " ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

//            List<Integer> pepIdList = new ArrayList<Integer>();
//            String selectPepId = "SELECT `peptide_id`  FROM `proteins_peptides_table` WHERE `exp_id` = ? AND `sequence` = ? ;";
//        Set<String> accessionList = new HashSet<String>();
            selectPepIdStat = conn.prepareStatement(selectProtAccession);
//            selectPepIdStat.setInt(1, datasetId);
            int index = 1;
            for (String str : searchSet) {
                selectPepIdStat.setString(index++, str);
            }
            ResultSet rs = selectPepIdStat.executeQuery();
            while (rs.next()) {
//                pepIdList.add(rs1.getInt("peptide_id"));
                String prot = rs.getString("protein");
                if (prot != null && !prot.equalsIgnoreCase("") && !prot.equalsIgnoreCase("SHARED PEPTIDE")) {
                    protAccessionQuerySet.add(prot);
                }
                String otherProt = rs.getString("other_protein(s)");
                if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(otherProt.split(",")));
                }
                String peptideProt = rs.getString("peptide_protein(s)");
                if (peptideProt != null && !peptideProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(peptideProt.split(",")));
                }
                expIds.add(rs.getInt("exp_id"));

            }
            rs.close();
//            System.out.println("prot acc number are "+protAccessionQuerySet);
            proteinsList = this.searchIdentificationProteinAllDatasetsByAccession(protAccessionQuerySet, validatedOnly);
            if (proteinsList == null) {
                return null;
            }
            for (int key : proteinsList.keySet()) {
                IdentificationProteinBean pb = proteinsList.get(key);
                if (expIds.contains(pb.getDatasetId())) {
                    filteredProteinsList.put(key, pb);
                }
            }

//            String selectPro = "SELECT `protein`  FROM `experiment_peptides_proteins_table`  WHERE `exp_id` = ? AND `peptide_id` = ? ;";
//            for (int key : pepIdList) {
//                if (conn == null || conn.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                selectProStat = conn.prepareStatement(selectPro);
//                selectProStat.setInt(1, datasetId);
//                selectProStat.setInt(2, key);
//                ResultSet rs_ = selectProStat.executeQuery();
//                while (rs_.next()) {
//                    accessionList.add(rs_.getString("protein"));
//                }
//                rs_.close();
//
//                for (String accKey : accessionList) {
//                    String[] AccArr = accKey.split(",");
//                    for (String str : AccArr) {
//                        if (str.length() > 3) {
//                            Map<Integer, IdentificationProteinBean> tempProteinsList = this.searchProteinByAccession(str.trim(), datasetId, validatedOnly);
//                            if (tempProteinsList != null) {
//                                proteinsList.putAll(tempProteinsList);
//                            }
//                        }
//                    }
//                }
//
//            }
            System.gc();
            return filteredProteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> SearchProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {
//        PreparedStatement selectProStat = null;
        PreparedStatement selectPepIdStat = null;
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<Integer, IdentificationProteinBean>();
        Map<Integer, IdentificationProteinBean> filteredProteinsList = new HashMap<Integer, IdentificationProteinBean>();

        String[] queryWordsArr = peptideSequenceKeyword.split("\n");
        StringBuilder sb = new StringBuilder();

        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }

        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`sequence` = ?");

        }
        Set<String> protAccessionQuerySet = new HashSet<String>();
        Set<Integer> expIds = new HashSet<Integer>();
        String selectProtAccession = "SELECT  `protein` ,  `other_protein(s)` ,  `peptide_protein(s)` , `exp_id` \n"
                + "FROM  `proteins_peptides_table`  WHERE " + (sb.toString()) + " AND `exp_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

//            List<Integer> pepIdList = new ArrayList<Integer>();
//            String selectPepId = "SELECT `peptide_id`  FROM `proteins_peptides_table` WHERE `exp_id` = ? AND `sequence` = ? ;";
//        Set<String> accessionList = new HashSet<String>();
            selectPepIdStat = conn.prepareStatement(selectProtAccession);
//            selectPepIdStat.setInt(1, datasetId);
            int index = 1;
            for (String str : searchSet) {
                selectPepIdStat.setString(index++, str);
            }
            selectPepIdStat.setInt(index, datasetId);
            ResultSet rs = selectPepIdStat.executeQuery();
            while (rs.next()) {
//                pepIdList.add(rs1.getInt("peptide_id"));
                String prot = rs.getString("protein");
                if (prot != null && !prot.equalsIgnoreCase("") && !prot.equalsIgnoreCase("SHARED PEPTIDE")) {
                    protAccessionQuerySet.add(prot);
                }
                String otherProt = rs.getString("other_protein(s)");
                if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(otherProt.split(",")));
                }
                String peptideProt = rs.getString("peptide_protein(s)");
                if (peptideProt != null && !peptideProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(peptideProt.split(",")));
                }
                expIds.add(rs.getInt("exp_id"));

            }
            rs.close();
            proteinsList = this.searchIdentificationProteinAllDatasetsByAccession(protAccessionQuerySet, validatedOnly);
            if (proteinsList == null || proteinsList.isEmpty()) {
                return null;
            }
            for (int key : proteinsList.keySet()) {
                IdentificationProteinBean pb = proteinsList.get(key);
                if (expIds.contains(pb.getDatasetId())) {

                    filteredProteinsList.put(key, pb);
                }
            }

//            String selectPro = "SELECT `protein`  FROM `experiment_peptides_proteins_table`  WHERE `exp_id` = ? AND `peptide_id` = ? ;";
//            for (int key : pepIdList) {
//                if (conn == null || conn.isClosed()) {
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                }
//                selectProStat = conn.prepareStatement(selectPro);
//                selectProStat.setInt(1, datasetId);
//                selectProStat.setInt(2, key);
//                ResultSet rs_ = selectProStat.executeQuery();
//                while (rs_.next()) {
//                    accessionList.add(rs_.getString("protein"));
//                }
//                rs_.close();
//
//                for (String accKey : accessionList) {
//                    String[] AccArr = accKey.split(",");
//                    for (String str : AccArr) {
//                        if (str.length() > 3) {
//                            Map<Integer, IdentificationProteinBean> tempProteinsList = this.searchProteinByAccession(str.trim(), datasetId, validatedOnly);
//                            if (tempProteinsList != null) {
//                                proteinsList.putAll(tempProteinsList);
//                            }
//                        }
//                    }
//                }
//
//            }
            System.gc();
            return filteredProteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    //Security Handling 
    /**
     * store new user details
     *
     * @param username user username
     * @param userPassword
     * @param admin user is admin or not
     * @param email user email
     *
     * @return test successful process
     */
    public synchronized boolean storeNewUser(String username, String userPassword, boolean admin, String email) {
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
            regUserStat.setString(2, userPassword);
            regUserStat.setString(3, "" + admin);
            regUserStat.setString(4, email);
            int rs = regUserStat.executeUpdate();
            if (rs > 0) {
                test = true;
            }

        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (SQLException e) {
        }

        return test;
    }

    /**
     * check user username is available
     *
     * @param email user email
     *
     * @return test successful process
     */
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
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return true;//not valid
    }

    /**
     * authenticate username and password for user
     *
     * @param email user email
     *
     * @return password
     */
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
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return null;//not valid
    }

    /**
     * get user details
     *
     * @param email user email
     *
     * @return user details of user
     */
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
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return null;//not valid
    }

    /**
     * get users list
     *
     * @return list of users
     */
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
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return usersList;//not valid
    }

    /**
     * remove username from the database
     *
     * @param username
     *
     * @return test boolean successful process
     */
    public synchronized boolean removeUser(String username) {
        try {

            PreparedStatement removeUserStat = null;
            String removeuser = "DELETE  FROM `users_table` WHERE `user_name` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            removeUserStat = conn.prepareStatement(removeuser);
            removeUserStat.setString(1, username.toUpperCase());
            int rs = removeUserStat.executeUpdate();
            if (rs > 0) {
                return true;//valid username

            }
            removeUserStat.clearParameters();
            removeUserStat.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        return false;//not valid
    }

    /**
     * update user password in the database
     *
     * @param username
     * @param newpassword
     * @return test boolean successful process
     */
    public synchronized boolean updateUserPassword(String username, String newpassword) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String updateProtDesc = "UPDATE  `" + dbName + "`.`users_table` SET `password`= ? WHERE `user_name` = ? ;";
            PreparedStatement updateProtDescStat = conn.prepareStatement(updateProtDesc);
            updateProtDescStat.setString(1, newpassword);
            updateProtDescStat.setString(2, username.toUpperCase());
            int test = updateProtDescStat.executeUpdate();
            updateProtDescStat.clearParameters();
            updateProtDescStat.close();
            if (test > 0) {
                return true;
            }
        } catch (ClassNotFoundException e2) {
            System.err.println(e2.getLocalizedMessage());
//            e2.printStackTrace();
        } catch (IllegalAccessException e2) {
            System.err.println(e2.getLocalizedMessage());
//            e2.printStackTrace();
        } catch (InstantiationException e2) {
            System.err.println(e2.getLocalizedMessage());
//            e2.printStackTrace();
        } catch (SQLException e2) {
            System.err.println(e2.getLocalizedMessage());
//            e2.printStackTrace();
        }

        return false;
    }

    /**
     * update fraction range data in the database
     *
     * @param dataset
     *
     * @return test boolean successful process
     */
    public boolean updateFractionRange(IdentificationDataset dataset) {
        List<Integer> fractionIDs = this.getFractionIdsList(dataset.getDatasetId());
        java.util.Collections.sort(fractionIDs);
//        Map<Integer, FractionBean> fractionRangeList = dataset.getFractionsList();
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
                fb = null;//fractionRangeList.get(x);
                updateFractionStat = conn.prepareStatement(updateFraction);
//                updateFractionStat.setDouble(1, fb.getMinRange());
//                updateFractionStat.setDouble(2, fb.getMaxRange());
                updateFractionStat.setInt(3, fb.getFractionIndex());
                updateFractionStat.setInt(4, fractId);
                updateFractionStat.executeUpdate();
                updateFractionStat.clearParameters();
                updateFractionStat.close();
            } catch (ClassNotFoundException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (InstantiationException e) {
                System.err.println("at error" + e.getLocalizedMessage());
                return false;
            } catch (SQLException e) {
                System.err.println("at error" + e.getLocalizedMessage());
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

            test = updateDatasetInformation(conn, dataset);	//update dataset table

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        }
        System.gc();
        return test;
    }

    /**
     * update fractions data for dataset
     *
     * @param connection
     * @param datasetId
     *
     * @return test boolean successful process
     */
    public synchronized boolean removeFractions(Connection connection, int datasetId) {
        PreparedStatement getFractDatasetStat = null;//done
        PreparedStatement remFractStat = null;//done
        PreparedStatement remFractDatasetStat = null;//done

        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(driver).newInstance();
                connection = DriverManager.getConnection(url + dbName, userName, password);
            }
            String remFract = "DELETE FROM `" + dbName + "`.`fractions_table`   WHERE  `fraction_id` =? ";
            String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";

            getFractDatasetStat = connection.prepareStatement(selectFractList);
            getFractDatasetStat.setInt(1, datasetId);
            ResultSet rs = getFractDatasetStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                fractionIdList.add(fraction_id);

            }

            for (int fb : fractionIdList) {
                remFractStat = connection.prepareStatement(remFract);
                remFractStat.setInt(1, fb);
                remFractStat.executeUpdate();
            }

            rs.close();

            String removeFraction = "DELETE FROM `experiment_fractions_table`   WHERE `exp_id` = ? ;";

            remFractDatasetStat = connection.prepareStatement(removeFraction);
            remFractDatasetStat.setInt(1, datasetId);
            remFractDatasetStat.executeUpdate();
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * store fractions data for dataset
     *
     * @param connection
     * @param dataset
     *
     * @return test boolean successful process
     */
    public synchronized boolean insertFractions(Connection connection, IdentificationDataset dataset) {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(driver).newInstance();
                connection = DriverManager.getConnection(url + dbName, userName, password);
            }

            String insertFractDataset = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            PreparedStatement insertFractDatasetStat = conn.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
            int fractId = 0;

//            for (FractionBean fb : dataset.getFractionsList().values()) {
//                insertFractDatasetStat = connection.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
//                insertFractDatasetStat.setInt(1, dataset.getDatasetId());
//                insertFractDatasetStat.setDouble(2, fb.getMinRange());
//                insertFractDatasetStat.setDouble(3, fb.getMaxRange());
//                insertFractDatasetStat.setInt(4, fb.getFractionIndex());
//                insertFractDatasetStat.executeUpdate();
//                ResultSet rs1 = insertFractDatasetStat.getGeneratedKeys();
//                while (rs1.next()) {
//                    fractId = rs1.getInt(1);
//                }
//                for (IdentificationProteinBean pb : fb.getProteinList().values()) {
//                    this.insertProteinFract(connection, fractId, pb);
//                }
//                rs1.close();
//            }
        } catch (ClassNotFoundException exc) {
            System.err.println(exc.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException exc) {
            System.err.println(exc.getLocalizedMessage());
            return false;
        } catch (InstantiationException exc) {
            System.err.println(exc.getLocalizedMessage());
            return false;
        } catch (SQLException exc) {
            System.err.println(exc.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * update fractions data for tempDataset from fraction file
     *
     * @param tempDataset
     * @param dataset
     *
     * @return test boolean successful process
     */
    public synchronized boolean updateProtFractionFile(IdentificationDataset tempDataset, IdentificationDataset dataset) {
        boolean test = true;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            tempDataset.setFractionsNumber(dataset.getFractionsNumber());

            test = updateDatasetInformation(conn, tempDataset);	//update tempDataset table
            //remove all fractions
            test = removeFractions(conn, dataset.getDatasetId());
            insertFractions(conn, dataset);

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        }
        return test;
    }

    /**
     * update fractions data for protein in database
     *
     * @param proteinBean
     * @param datasetId
     * @return test boolean
     */
    public boolean updateDatasetProteinFraction(IdentificationProteinBean proteinBean, int datasetId) {

        String updateProtFraction = "UPDATE  `" + dbName + "`.`experiment_protein_table` SET `spectrum_fraction_spread_upper_range_kDa`=? ,`spectrum_fraction_spread_lower_range_kDa`=? ,`peptide_fraction_spread_upper_range_kDa`=? , `peptide_fraction_spread_lower_range_kDa`=?   WHERE `exp_id` = ? AND `prot_accession`=?;";
        boolean test = false;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updateProtFractionStat = conn.prepareStatement(updateProtFraction);
            updateProtFractionStat.setString(1, proteinBean.getSpectrumFractionSpread_upper_range_kDa());
            updateProtFractionStat.setString(2, proteinBean.getSpectrumFractionSpread_lower_range_kDa());
            updateProtFractionStat.setString(3, proteinBean.getPeptideFractionSpread_upper_range_kDa());
            updateProtFractionStat.setString(4, proteinBean.getPeptideFractionSpread_lower_range_kDa());
            updateProtFractionStat.setInt(5, datasetId);
            updateProtFractionStat.setString(6, proteinBean.getAccession());
            updateProtFractionStat.executeUpdate();
            test = true;
            updateProtFractionStat.clearParameters();
            updateProtFractionStat.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            test = false;
        }
        return test;

    }

    /**
     * get peptides data for a database using peptides ids
     *
     * @param peptideIds
     * @return list of peptides
     */
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
                    pepb.setValidated(rs.getDouble("validated"));
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

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        System.gc();
        return peptidesList;
    }

    /**
     * store peptide data for a protein in database
     *
     * @param datasetId
     * @param peptideId
     * @param accession
     * @param connection
     *
     * @return integer
     */
    private int insertDatasetProteinPeptide(int datasetId, int peptideId, String accession, Connection connection, int z) {
        int test = -1;
        try {

            if (connection == null || connection.isClosed()) {
                Class.forName(driver).newInstance();
                connection = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertDatasetProtPeptQ = "INSERT INTO  `" + dbName + "`.`experiment_peptides_proteins_table` (`exp_id` ,`peptide_id`,`protein`)VALUES (?,?,?);";
            PreparedStatement insertDatasetProtPeptQStat = connection.prepareStatement(insertDatasetProtPeptQ);
            insertDatasetProtPeptQStat.setInt(1, datasetId);
            insertDatasetProtPeptQStat.setInt(2, peptideId);
            insertDatasetProtPeptQStat.setString(3, accession.toUpperCase());
            test = insertDatasetProtPeptQStat.executeUpdate();
            insertDatasetProtPeptQStat.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }

        return test;
    }

    /**
     * get peptides id list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @return peptides id list for the selected protein group in the selected
     * dataset
     */
    public Set<Integer> getDatasetPepProIds(int datasetId, String accession) {
        PreparedStatement selectDatasetProPepStat = null;
        String selectDatasetProPep = "SELECT `peptide_id` FROM `experiment_peptides_proteins_table` WHERE `exp_id`=? AND `protein` LIKE(?)";
        Set<Integer> datasetProPepIds = new HashSet<Integer>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetProPepStat = conn.prepareStatement(selectDatasetProPep);
            selectDatasetProPepStat.setInt(1, datasetId);
            selectDatasetProPepStat.setString(2, "%" + accession.toUpperCase() + "%");
            ResultSet rs = selectDatasetProPepStat.executeQuery();
            while (rs.next()) {
                datasetProPepIds.add(rs.getInt("peptide_id"));
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        System.gc();
        return datasetProPepIds;
    }

    /**
     * read and store standard plot files in the database
     *
     *
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     */
    public boolean updateStandardPlotProt(IdentificationDataset dataset) {
        removeStandarPlot(dataset.getDatasetId());
        for (StandardProteinBean spb : dataset.getStanderdPlotProt()) {
            insertStandardPlotProtein(dataset.getDatasetId(), spb);
        }
        System.gc();
        return true;
    }

    /**
     * store standard plot bean in the database
     *
     *
     * @param datasetId dataset id
     * @param standardProteinBean
     * @return test boolean
     */
    public boolean insertStandardPlotProtein(int datasetId, StandardProteinBean standardProteinBean) {
        int check = -1;
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertStandPlotQ = "INSERT INTO  `" + dbName + "`.`standard_plot_proteins`(`exp_id` ,`mw_(kDa)`,`name`,`lower`,`upper`,`color`)VALUES (?,?,?,?,?,?);";
            PreparedStatement insertStandPlotStat = conn.prepareStatement(insertStandPlotQ);
            insertStandPlotStat.setInt(1, datasetId);
            insertStandPlotStat.setDouble(2, standardProteinBean.getMW_kDa());
            insertStandPlotStat.setString(3, standardProteinBean.getName().toUpperCase());
            insertStandPlotStat.setInt(4, standardProteinBean.getLowerFraction());
            insertStandPlotStat.setInt(5, standardProteinBean.getUpperFraction());
            insertStandPlotStat.setString(6, standardProteinBean.getColor().toUpperCase());
            check = insertStandPlotStat.executeUpdate();
            insertStandPlotStat.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        }
        return check > 0;

    }

    /**
     * remove standard plot data in the database
     *
     *
     * @param datasetId dataset id
     * @return test boolean
     */
    public boolean removeStandarPlot(int datasetId) {
        int x = 0;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String StandarPlot = "DELETE FROM `standard_plot_proteins`  WHERE  `exp_id`=? ";

            PreparedStatement remDatasetStat = conn.prepareStatement(StandarPlot);
            remDatasetStat.setInt(1, datasetId);
            x = remDatasetStat.executeUpdate();
            System.gc();
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
        return x > 0;
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     * @return quantPeptidetList
     */
    public List<StandardProteinBean> getStandardProtPlotList(int datasetId) {
        List<StandardProteinBean> standardPlotList = new ArrayList<StandardProteinBean>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectPeptide = "SELECT * FROM `standard_plot_proteins` where `exp_id` = ?;";
            PreparedStatement selectPeptideStat = conn.prepareStatement(selectPeptide);
            selectPeptideStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideStat.executeQuery();
            while (rs.next()) {
                StandardProteinBean spb = new StandardProteinBean();
                spb.setMW_kDa(rs.getDouble("mw_(kDa)"));
                spb.setLowerFraction(rs.getInt("lower"));
                spb.setUpperFraction(rs.getInt("upper"));
                spb.setName(rs.getString("name"));
                spb.setColor(rs.getString("color"));
                standardPlotList.add(spb);
            }
            rs.close();
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return standardPlotList;
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param dataset
     * @return test boolean
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public boolean updateDatasetData(IdentificationDataset dataset) {

        String updateExp = "UPDATE  `" + dbName + "`.`experiments_table`  SET `name`=?,`ready`=? ,`uploaded_by`=?,`species`=?,`sample_type`=?,`sample_processing`=?,`instrument_type`=?,`frag_mode` =?,`proteins_number` = ? ,	`email` =?,`pblication_link`=?,`description`=?,`peptides_number` =?  WHERE `exp_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updateExpStat = conn.prepareStatement(updateExp, Statement.RETURN_GENERATED_KEYS);
            updateExpStat.setString(1, dataset.getName().toUpperCase());
            updateExpStat.setInt(2, 2);
            updateExpStat.setString(3, dataset.getUploadedByName().toUpperCase());
            updateExpStat.setString(4, dataset.getSpecies());
            updateExpStat.setString(5, dataset.getSampleType());
            updateExpStat.setString(6, dataset.getSampleProcessing());
            updateExpStat.setString(7, dataset.getInstrumentType());
            updateExpStat.setString(8, dataset.getFragMode());
            updateExpStat.setInt(9, dataset.getProteinsNumber());
            updateExpStat.setString(10, dataset.getEmail().toUpperCase());
            if (dataset.getPublicationLink() != null) {
                updateExpStat.setString(11, dataset.getPublicationLink());
            } else {
                updateExpStat.setString(11, "NOT AVAILABLE");
            }
            updateExpStat.setString(12, dataset.getDescription());
            updateExpStat.setInt(13, dataset.getPeptidesNumber());
            updateExpStat.setInt(14, dataset.getDatasetId());
            int test = updateExpStat.executeUpdate();
            System.gc();
            if (test > 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        }

        return false;
    }

    /**
     * store glycopeptides data for dataset in the database
     *
     *
     * @param peptideBean
     * @return test boolean successful process
     */
    private boolean updateGlycoPeptid(PeptideBean peptideBean) {

        String updatePeptide = "UPDATE  `" + dbName + "`.`proteins_peptides_table`  SET  `glycopattern_position(s)` = ?, `deamidation_and_glycopattern` = ?  WHERE  peptide_id=?;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updatePePStat = conn.prepareStatement(updatePeptide, Statement.RETURN_GENERATED_KEYS);
            updatePePStat.setString(1, peptideBean.getGlycopatternPositions());
            updatePePStat.setString(2, peptideBean.getDeamidationAndGlycopattern().toString());
            updatePePStat.setInt(3, peptideBean.getPeptideId());

            int test = updatePePStat.executeUpdate();
            if (test > 0) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
            return false;
        }

        return false;

    }

    /**
     * store peptides data for dataset in the database
     * @param peptidesList
     * @return test boolean successful process
     */
    @SuppressWarnings("SleepWhileInLoop")
    public synchronized boolean setPeptideFile(Map<Integer, PeptideBean> peptidesList) {
        int counter = 0;
        boolean test = false;
        try {
            for (PeptideBean pb : peptidesList.values()) {

                test = this.updateGlycoPeptid(pb);
                counter++;

                if (counter == 10000) {
                    conn.close();
                    Thread.sleep(100);
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                    counter = 0;
                }
            }
        } catch (ClassNotFoundException exp) {
        } catch (IllegalAccessException exp) {
        } catch (InstantiationException exp) {
        } catch (InterruptedException exp) {
        } catch (SQLException exp) {
        }
        return test;
    }

    
    // ==================================================  quant data  ===================================================
    
     public QuantDatasetListObject getQuantDatasetListObject() {

        Set<QuantDatasetObject> quantDatasetList = new HashSet<QuantDatasetObject>();
        boolean[] activeHeaders = new boolean[27];

        try {
            PreparedStatement selectStudiesStat = null;
            String selectStudies = "SELECT * FROM  `combined_dataset_table` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            ResultSet rs = selectStudiesStat.executeQuery();
            while (rs.next()) {
                QuantDatasetObject pb = new QuantDatasetObject();
                String author = rs.getString("author");
                if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                    activeHeaders[0] = true;
                }
                pb.setAuthor(author);
                int year = rs.getInt("year");
                if (!activeHeaders[1] && year != 0) {
                    activeHeaders[1] = true;
                }
                pb.setYear(year);
                 int identified_proteins_num = rs.getInt("identified _proteins_num");
                if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
                    activeHeaders[2] = true;
                }
                pb.setIdentifiedProteinsNumber(identified_proteins_num);
                
                int quantified_protein_num = rs.getInt("quantified_protein_num");
                if (!activeHeaders[3] && quantified_protein_num != -1) {
                    activeHeaders[3] = true;
                }
                pb.setQuantifiedProteinsNumber(quantified_protein_num);
                

                String disease_group = rs.getString("disease_group");
                if (!activeHeaders[4] && disease_group != null && !disease_group.equalsIgnoreCase("Not Available")) {
                    activeHeaders[4] = true;
                }
                pb.setDiseaseGroups(disease_group);
                
                
                String raw_data_url = rs.getString("raw_data_url");
                if (!activeHeaders[5] && raw_data_url != null && !raw_data_url.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                pb.setRawDataUrl(raw_data_url);
                
                
                
                

                int files_num = rs.getInt("files_num");
                if (!activeHeaders[6] && files_num != -1) {
                    activeHeaders[6] = true;
                }
                pb.setFilesNumber(files_num);

                String type_of_study = rs.getString("type_of_study");
                if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                    activeHeaders[7] = true;
                }
                pb.setTypeOfStudy(type_of_study);
                

                String sample_type = rs.getString("sample_type");
                if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                    activeHeaders[8] = true;
                }
                pb.setSampleType(sample_type);
                

                String sample_matching = rs.getString("sample_matching");
                if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                    activeHeaders[9] = true;
                }
                pb.setSampleMatching(sample_matching);
                
                  String shotgun_targeted = rs.getString("shotgun_targeted");
                if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                    activeHeaders[10] = true;
                }
                pb.setShotgunTargeted(shotgun_targeted);
                
                
                

                String technology = rs.getString("technology");
                if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                    activeHeaders[11] = true;
                }
                pb.setTechnology(technology);
                

                String analytical_approach = rs.getString("analytical_approach");
                if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                    activeHeaders[12] = true;
                }
                pb.setAnalyticalApproach(analytical_approach);

                String enzyme = rs.getString("enzyme");
                if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                    activeHeaders[13] = true;
                }
                pb.setEnzyme(enzyme);

              

                String quantification_basis = rs.getString("quantification_basis");
                if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                    activeHeaders[14] = true;
                }

                pb.setQuantificationBasis(quantification_basis);

                String quant_basis_comment = rs.getString("quant_basis_comment");
                if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[15] = true;
                }
                pb.setQuantBasisComment(quant_basis_comment);

               

                int id = rs.getInt("index");
                pb.setUniqId(id-1);

                String normalization_strategy = rs.getString("normalization_strategy");
                if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                    activeHeaders[16] = true;
                }
                pb.setNormalizationStrategy(normalization_strategy);

                
                String pumed_id = rs.getString("pumed_id");
                if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                    activeHeaders[17] = true;
                }
                pb.setPumedID(pumed_id);
                
                  String patient_group_i = rs.getString("patient_group_i");
                if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[18] = true;
                }
                pb.setPatientsGroup1(patient_group_i);
                
                
                 int patients_group_i_number = rs.getInt("patients_group_i_number");
                if (!activeHeaders[19] && patients_group_i_number != -1) {
                    activeHeaders[19] = true;
                }
                pb.setPatientsGroup1Number(patients_group_i_number);
                
                 String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[20] = true;
                }
                pb.setPatientsGroup1Comm(patient_gr_i_comment);

                String patient_sub_group_i = rs.getString("patient_sub_group_i");
                if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[21] = true;
                }
                pb.setPatientsSubGroup1(patient_sub_group_i);

                String patient_group_ii = rs.getString("patient_group_ii");
                if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[22] = true;
                }
                pb.setPatientsGroup2(patient_group_ii);

                int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                if (!activeHeaders[23] && patients_group_ii_number != -1) {
                    activeHeaders[23] = true;
                }
                pb.setPatientsGroup2Number(patients_group_ii_number);
                
                 String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[24]= true;
                }
                pb.setPatientsGroup2Comm(patient_gr_ii_comment);
                
                 String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[25]= true;
                }
                pb.setPatientsSubGroup2(patient_sub_group_ii);
                
                 String additional_comments = rs.getString("additional_comments");
                if (!activeHeaders[26] && additional_comments != null && !additional_comments.equalsIgnoreCase("Not Available")) {
                    activeHeaders[26]= true;
                }
                pb.setAdditionalcomments(additional_comments);
                
                
                quantDatasetList.add(pb);
                
            }
            rs.close();
            QuantDatasetListObject datasetObject = new QuantDatasetListObject();
            QuantDatasetObject[] dss = new QuantDatasetObject[quantDatasetList.size()];
//            int i = 0;
            for (QuantDatasetObject ds : quantDatasetList) {
//                ds.setUniqId(i);
                dss[ds.getUniqId()] = ds;
//                i++;
            }

            datasetObject.setQuantDatasetList(dss);
            datasetObject.setActiveHeaders(activeHeaders);
            return datasetObject;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();

        return null;

    }

    public boolean[] getActiveFilters() {
        String[] columnArr = new String[]{"`identified _proteins_num`", "`quantified_protein_num`", "`disease_group`", "`raw_data_url`", "`year`", "`type_of_study`", "`sample_type`", "`sample_matching`", "`technology`", "`analytical_approach`", "`enzyme`", "`shotgun_targeted`", "`quantification_basis`", "`quant_basis_comment`", "`patients_group_i_number`", "`patients_group_ii_number`", "`normalization_strategy`"};
        boolean[] activeFilters = new boolean[columnArr.length];

        try {

            PreparedStatement selectPumed_idStat = null;
            String selectPumed_id = "SELECT  `pumed_id` FROM  `quant_dataset_table` GROUP BY  `pumed_id` ORDER BY  `pumed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            ResultSet rs = selectPumed_idStat.executeQuery();
            int pumed_id_index = 0;
            while (rs.next()) {
                pumed_id_index++;
            }
            rs.close();

            /// check the colums one by one 
            for (int index = 0; index < columnArr.length; index++) {
                String selectPumed_id1 = "SELECT  `pumed_id` FROM  `quant_dataset_table` GROUP BY  `pumed_id`, " + columnArr[index] + " ORDER BY  `pumed_id` ";
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPumed_idStat = conn.prepareStatement(selectPumed_id1);
                rs = selectPumed_idStat.executeQuery();
                int pumed_id_com_index = 0;
                while (rs.next()) {
                    pumed_id_com_index++;
                }
                rs.close();
                if (pumed_id_index != pumed_id_com_index) {
                    activeFilters[index] = true;

                }

            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        activeFilters[0] = false;
        activeFilters[1] = false;
        activeFilters[2] = false;
        activeFilters[3] = true;
        activeFilters[4] = true;
        activeFilters[7] = false;
        activeFilters[activeFilters.length - 2] = false;
        activeFilters[activeFilters.length - 3] = false;
        activeFilters[activeFilters.length - 4] = false;

        return activeFilters;

    }
    
    private void singleuseUpdateDb() {
        String stat = "ALTER TABLE  `fractions_table` ADD  `exp_id` INT( 255 ) NOT NULL DEFAULT  '0';";
        Map<Integer, Integer> fractionIdToExp = new HashMap<Integer, Integer>();
        PreparedStatement getFractDatasetStat = null;//done

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            Statement st = conn.createStatement();
            //CREATE TABLE  `users_table`
            st.executeUpdate(stat);

            System.out.println(" success 1");

            String selectFractList = "SELECT * FROM `experiment_fractions_table`";

            getFractDatasetStat = conn.prepareStatement(selectFractList);
            ResultSet rs = getFractDatasetStat.executeQuery();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                int expId = rs.getInt("exp_id");
                fractionIdToExp.put(fraction_id, expId);

            }
            rs.close();

            System.out.println(" success 2");
            for (int fb : fractionIdToExp.keySet()) {
                int expId = fractionIdToExp.get(fb);
                String updateFraction = "UPDATE  `" + dbName + "`.`fractions_table` SET `exp_id`=?   WHERE `fraction_id` = ?";
                PreparedStatement updateFractionStat = conn.prepareStatement(updateFraction);
                updateFractionStat.setInt(1, expId);
                updateFractionStat.setInt(2, fb);
                updateFractionStat.executeUpdate();
//                        updateFractionStat.clearParameters();              

            }
//            
//                System.out.println(" success 3");
//            String dropFractionExpTable = "DROP TABLE `experiment_fractions_table`";
//            PreparedStatement updateFractionStat = conn.prepareStatement(dropFractionExpTable);  
//            updateFractionStat.executeUpdate();

            //update peptide table
            String peptideTab = "ALTER TABLE  `proteins_peptides_table` ADD  `main_prot_desc` VARCHAR( 500 ) NOT NULL ";

            Statement stst = conn.createStatement();
            //update peptable`
            stst.executeUpdate(peptideTab);

            System.out.println(" success 4");

//                String selectPro = "SELECT * FROM `experiment_protein_table` ;";
//                  if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//            PreparedStatement selectProStat = conn.prepareStatement(selectPro);
//          
//            ResultSet rs2 = selectProStat.executeQuery();
//          Map<Integer, IdentificationProteinBean> proteinsList=fillProteinInformation(rs2);
//            System.gc();
//            rs2.close();
//            conn.close();
//                    Thread.sleep(100);
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//            System.out.println("start updating process");
//            PreparedStatement updatePepStat = null;
//            int index = 0;
//            System.out.println(" - " + proteinsList.size());
//
//            for (IdentificationProteinBean pb : proteinsList.values()) {
//////                System.out.print(" - " + index);
//                String updatePep = "UPDATE  `" + dbName + "`.`proteins_peptides_table` SET `main_prot_desc`=?   WHERE `protein` = ? AND `other_protein(s)` = ? AND `exp_id` = ?";
//                updatePepStat = conn.prepareStatement(updatePep);
//                updatePepStat.setString(1, pb.getDescription());
//                updatePepStat.setString(2, pb.getAccession());
//                updatePepStat.setString(3, pb.getOtherProteins());
//                updatePepStat.setInt(4, pb.getDatasetId());
//                updatePepStat.executeUpdate();
//                updatePepStat.close();                
//                index++;
//                if (index == 10000) {
//                    System.out.println("need break :-( ");
//                    index = 0;
//                     Thread.sleep(100);
//                    Class.forName(driver).newInstance();
//                    conn = DriverManager.getConnection(url + dbName, userName, password);
//                    System.out.println("back :-D ");
//                }
//
//            }
//            
//                System.out.println(" success 5");
//            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public boolean storeQuantProt(List<QuantProtein> qProtList) {
        System.out.println("start store data");
        boolean success = true;
        String insertQProt = "INSERT INTO  `" + dbName + "`.`quant_prot_table` (`pumed_id` ,`uniprot_accession` ,`uniprot_protein_name` ,`publication_acc_number` ,`publication_protein_name` ,`raw_data_available` ,`type_of_study` ,"
                + "`sample_type` ,`patient_group_i` ,`patient_sub_group_i` ,`patient_gr_i_comment` ,`patient_group_ii` ,`patient_sub_group_ii` ,`patient_gr_ii_comment` ,`sample_matching` ,`normalization_strategy` ,`technology`,`analytical_approach`,`enzyme`,`shotgun_targeted`,`quantification_basis`,`quant_basis_comment`,`additional_comments`,`q_peptide_key`,`peptide_sequance`,`peptide_modification`,`modification_comment` ,`string_fc_value`,`string_p_value`,`peptideId_number`,`quantified_peptides_number`,`patients_group_i_number`,`patients_group_ii_number`,`p_value`,`roc_auc`,`fc_value`,`peptide_prot`)VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement insertQProtStat = null;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (QuantProtein qprot : qProtList) {
                insertQProtStat = conn.prepareStatement(insertQProt, Statement.RETURN_GENERATED_KEYS);
                insertQProtStat.setString(1, qprot.getPumedID().toUpperCase());
                insertQProtStat.setString(2, qprot.getUniprotAccession().toUpperCase());
                insertQProtStat.setString(3, qprot.getUniprotProteinName().toUpperCase());
                insertQProtStat.setString(4, qprot.getPublicationAccNumber().toUpperCase());
                insertQProtStat.setString(5, qprot.getPublicationProteinName().toUpperCase());
                insertQProtStat.setString(6, qprot.getRawDataAvailable().toUpperCase());
                insertQProtStat.setString(7, qprot.getTypeOfStudy().toUpperCase());
                insertQProtStat.setString(8, qprot.getSampleType().toUpperCase());
                insertQProtStat.setString(9, qprot.getPatientGroupI().toUpperCase());
                insertQProtStat.setString(10, qprot.getPatientSubGroupI().toUpperCase());
                insertQProtStat.setString(11, qprot.getPatientGrIComment().toUpperCase());
                insertQProtStat.setString(12, qprot.getPatientGroupII().toUpperCase());
                insertQProtStat.setString(13, qprot.getPatientSubGroupII().toUpperCase());
                insertQProtStat.setString(14, qprot.getPatientGrIIComment().toUpperCase());
                insertQProtStat.setString(15, qprot.getSampleMatching().toUpperCase());
                insertQProtStat.setString(16, qprot.getNormalizationStrategy().toUpperCase());
                insertQProtStat.setString(17, qprot.getTechnology().toUpperCase());
                insertQProtStat.setString(18, qprot.getAnalyticalApproach().toUpperCase());
                insertQProtStat.setString(19, qprot.getEnzyme().toUpperCase());
                insertQProtStat.setString(20, qprot.getShotgunOrTargetedQquant());
                insertQProtStat.setString(21, qprot.getQuantificationBasis());
                insertQProtStat.setString(22, qprot.getQuantBasisComment());
                insertQProtStat.setString(23, qprot.getAdditionalComments());

                insertQProtStat.setString(24, qprot.getqPeptideKey());
                insertQProtStat.setString(25, qprot.getPeptideSequance());
                insertQProtStat.setString(26, qprot.getPeptideModification());
                insertQProtStat.setString(27, qprot.getModificationComment());
                insertQProtStat.setString(28, qprot.getStringFCValue());
                insertQProtStat.setString(29, qprot.getStringPValue());

                Integer num = qprot.getQuantifiedProteinsNumber();

                insertQProtStat.setInt(30, num);

                num = qprot.getPeptideIdNumb();
                insertQProtStat.setInt(31, num);

                num = qprot.getQuantifiedPeptidesNumber();
                insertQProtStat.setInt(32, num);

                num = qprot.getPatientsGroupINumber();
                insertQProtStat.setInt(33, num);
                num = qprot.getPatientsGroupIINumber();

                insertQProtStat.setInt(34, num);

                Double dnum = qprot.getpValue();
                insertQProtStat.setDouble(35, dnum);

                dnum = qprot.getRocAuc();
                insertQProtStat.setDouble(36, dnum);

                dnum = qprot.getFcPatientGroupIonPatientGroupII();
                insertQProtStat.setDouble(37, dnum);
                insertQProtStat.setString(38, String.valueOf(qprot.isPeptideProt()));

                insertQProtStat.executeUpdate();

                insertQProtStat.clearParameters();
                insertQProtStat.close();

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            success = false;
        } catch (Exception exp) {
            exp.printStackTrace();
            success = false;
        }

        return success;

    }
    private Map<String, QuantDatasetObject> authormap = new HashMap<String, QuantDatasetObject>();
    private void initPublications() {
        QuantDatasetObject qa1 = new QuantDatasetObject();
        qa1.setYear(2010);
        qa1.setFilesNumber(10);
        qa1.setDiseaseGroups("Disease Group I");
        qa1.setAuthor("Comabella, Manuel, et al.");

        authormap.put("20237129", qa1);

        QuantDatasetObject qa2 = new QuantDatasetObject();
        qa2.setYear(2010);
        qa2.setAuthor("Harris, Violaine K., et al.");
        qa2.setFilesNumber(15);
        qa2.setDiseaseGroups("Disease Group II");
        authormap.put("20600910", qa2);

        QuantDatasetObject qa3 = new QuantDatasetObject();
        qa3.setYear(2012);
        qa3.setFilesNumber(13);
        qa3.setDiseaseGroups("Disease Group II");
        qa3.setAuthor("Dhaunchak, Ajit Singh, et al.");
        authormap.put("22473675", qa3);

        QuantDatasetObject qa4 = new QuantDatasetObject();
        qa4.setYear(2012);
        qa4.setFilesNumber(14);
        qa4.setDiseaseGroups("Disease Group I");
        qa4.setAuthor("Jia, Yan et al.");
        authormap.put("22846148", qa4);

        QuantDatasetObject qa5 = new QuantDatasetObject();
        qa5.setYear(2013);
        qa5.setFilesNumber(5);
        qa5.setDiseaseGroups("Disease Group III");
        qa5.setAuthor("Kroksveen, Ann C., et al.");
        authormap.put("23059536", qa5);

        QuantDatasetObject qa6 = new QuantDatasetObject();
        qa6.setYear(2012);
        qa6.setFilesNumber(16);
        qa6.setAuthor("Kroksveen, A. C., et al.");
        qa6.setDiseaseGroups("Disease Group II");
        authormap.put("23278663", qa6);

        String selectPro = "SELECT DISTINCT  `pumed_id` ,  `quantified_proteins_number` , `raw_data_available` ,  `type_of_study` ,  `sample_type` ,  `sample_matching` ,  `normalization_strategy` ,  `technology` ,  `analytical_approach` ,  `enzyme` ,  `shotgun_targeted` ,  `quantification_basis` ,  `quant_basis_comment`  ,  `patients_group_i_number` ,  `patients_group_ii_number` ,  `patient_group_i` ,  `patient_gr_i_comment` ,  `patient_sub_group_i` ,  `patient_group_ii` ,  `patient_sub_group_ii` , `patient_gr_ii_comment` \n"
                + "FROM  `quant_prot_table` ";

        PreparedStatement selectProStat = null;
//        List<QuantProtein> quantProtResultList = null;
        Set<QuantDatasetObject> pubmidIds = new HashSet<QuantDatasetObject>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            ResultSet rs = selectProStat.executeQuery();

            int x = 0;
            while (rs.next()) {
                QuantDatasetObject pb = new QuantDatasetObject();
                pb.setPumedID(rs.getString("pumed_id"));
                pb.setQuantifiedProteinsNumber(rs.getInt("quantified_proteins_number"));
                pb.setDiseaseGroups(authormap.get(pb.getPumedID()).getDiseaseGroups());
                pb.setAuthor(authormap.get(pb.getPumedID()).getAuthor());
                pb.setYear(authormap.get(pb.getPumedID()).getYear());
                pb.setFilesNumber(authormap.get(pb.getPumedID()).getFilesNumber());
                pb.setTypeOfStudy(rs.getString("type_of_study"));
                pb.setSampleType(rs.getString("sample_type"));
                pb.setSampleMatching(rs.getString("sample_matching"));
                pb.setTechnology(rs.getString("technology"));
                pb.setAnalyticalApproach(rs.getString("analytical_approach"));
                pb.setEnzyme(rs.getString("enzyme"));
                pb.setShotgunTargeted(rs.getString("shotgun_targeted"));
                pb.setQuantificationBasis(rs.getString("quantification_basis"));
                pb.setQuantBasisComment(rs.getString("quant_basis_comment"));
                pb.setPatientsGroup1Number(rs.getInt("patients_group_i_number"));
                pb.setPatientsGroup2Number(rs.getInt("patients_group_ii_number"));
                pb.setIdentifiedProteinsNumber(0);

                pb.setRawDataUrl(rs.getString("raw_data_available"));
                pb.setNormalizationStrategy(rs.getString("normalization_strategy"));
                pb.setPatientsGroup1(rs.getString("patient_group_i"));
                pb.setPatientsGroup1Comm(rs.getString("patient_gr_i_comment"));
                pb.setPatientsSubGroup1(rs.getString("patient_sub_group_i"));
                pb.setPatientsGroup2(rs.getString("patient_group_ii"));
                pb.setPatientsGroup2Comm(rs.getString("patient_gr_ii_comment"));
                pb.setPatientsSubGroup2(rs.getString("patient_sub_group_ii"));
                pubmidIds.add(pb);
                x++;
            }

            System.out.println("start updating publications");
            String insertQProt = "INSERT INTO  `" + dbName + "`.`quant_dataset_table` (`pumed_id` ,\n"
                    + "`files_num` ,\n"
                    + "`identified _proteins_num` ,\n"
                    + "`quantified_protein_num` ,\n"
                    + "`disease_group` ,\n"
                    + "`raw_data_url` ,\n"
                    + "`year` ,\n"
                    + "`type_of_study` ,\n"
                    + "`sample_type` ,\n"
                    + "`sample_matching` ,\n"
                    + "`technology` ,\n"
                    + "`analytical_approach` ,\n"
                    + "`enzyme` ,\n"
                    + "`shotgun_targeted` ,\n"
                    + "`quantification_basis` ,\n"
                    + "`quant_basis_comment` ,\n"
                    + "`patients_group_i_number` ,\n"
                    + "`patients_group_ii_number` ,  `normalization_strategy`"
                    + ",`patient_group_i`,`patient_gr_i_comment`,`patient_sub_group_i`,`patient_group_ii`,`patient_gr_ii_comment`,`patient_sub_group_ii`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement insertPbublicationStat = null;

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (QuantDatasetObject pb : pubmidIds) {
                insertPbublicationStat = conn.prepareStatement(insertQProt, Statement.RETURN_GENERATED_KEYS);
                insertPbublicationStat.setString(1, pb.getPumedID());
                insertPbublicationStat.setInt(2, pb.getFilesNumber());
                insertPbublicationStat.setInt(3, pb.getIdentifiedProteinsNumber());
                insertPbublicationStat.setInt(4, pb.getQuantifiedProteinsNumber());

                insertPbublicationStat.setString(5, pb.getDiseaseGroups());
                insertPbublicationStat.setString(6, pb.getRawDataUrl());
                insertPbublicationStat.setInt(7, pb.getYear());

                insertPbublicationStat.setString(8, pb.getTypeOfStudy());
                insertPbublicationStat.setString(9, pb.getSampleType());
                insertPbublicationStat.setString(10, pb.getSampleMatching());
                insertPbublicationStat.setString(11, pb.getTechnology());
                insertPbublicationStat.setString(12, pb.getAnalyticalApproach());
                insertPbublicationStat.setString(13, pb.getEnzyme().toUpperCase());
                if (pb.getShotgunTargeted() == null) {
                    pb.setShotgunTargeted("Not Available");
                }
                insertPbublicationStat.setString(14, pb.getShotgunTargeted());

                if (pb.getQuantificationBasis() == null) {
                    pb.setQuantificationBasis("Not Available");
                }
                insertPbublicationStat.setString(15, pb.getQuantificationBasis());
                if (pb.getQuantBasisComment() == null) {
                    pb.setQuantBasisComment("Not Available");
                }
                insertPbublicationStat.setString(16, pb.getQuantBasisComment());
                insertPbublicationStat.setInt(17, pb.getPatientsGroup1Number());
                insertPbublicationStat.setInt(18, pb.getPatientsGroup2Number());
                insertPbublicationStat.setString(19, pb.getNormalizationStrategy());
                if (pb.getPatientsGroup1() == null) {
                    pb.setPatientsGroup1("Not Available");
                }
                insertPbublicationStat.setString(20, pb.getPatientsGroup1());
                if (pb.getPatientsGroup1Comm() == null) {
                    pb.setPatientsGroup1Comm("Not Available");
                }
                insertPbublicationStat.setString(21, pb.getPatientsGroup1Comm());

                if (pb.getPatientsSubGroup1() == null) {
                    pb.setPatientsSubGroup1("Not Available");
                }
                insertPbublicationStat.setString(22, pb.getPatientsSubGroup1());

                if (pb.getPatientsGroup2() == null) {
                    pb.setPatientsGroup2("Not Available");
                }
                insertPbublicationStat.setString(23, pb.getPatientsGroup2());
                if (pb.getPatientsGroup2Comm() == null) {
                    pb.setPatientsGroup2Comm("Not Available");
                }
                insertPbublicationStat.setString(24, pb.getPatientsGroup2Comm());

                if (pb.getPatientsSubGroup2() == null) {
                    pb.setPatientsSubGroup2("Not Available");
                }
                insertPbublicationStat.setString(25, pb.getPatientsSubGroup2());

                insertPbublicationStat.executeUpdate();
                insertPbublicationStat.clearParameters();
                insertPbublicationStat.close();

            }

//            String updateDSStat = "UPDATE  `csf_db_v2`.`quant_dataset_table` SET  `patient_group_ii` =  ? AND `patient_sub_group_ii` =? AND `patient_gr_ii_comment` =? WHERE  `quant_dataset_table`.`pumed_id` =? AND `type_of_study` =? AND `sample_type` =? AND `sample_matching` =? AND `technology` =? AND `analytical_approach` =? AND `enzyme` =? AND `shotgun_targeted` =? AND `quantification_basis` =? AND `quant_basis_comment` =? AND `patients_group_i_number` =? AND `patients_group_ii_number` =? AND `normalization_strategy` =?;";
//
//              PreparedStatement insertPbublicationStat = null;
//
//            if (conn == null || conn.isClosed()) {
//                Class.forName(driver).newInstance();
//                conn = DriverManager.getConnection(url + dbName, userName, password);
//            }
//
//            for (QuantDatasetObject pb : pubmidIds) {
//                insertPbublicationStat = conn.prepareStatement(updateDSStat);
//                insertPbublicationStat.setString(1, pb.getPatientsGroup2());
//                insertPbublicationStat.setString(2, pb.getPatientsSubGroup2());
//                insertPbublicationStat.setString(3, pb.getPatientsGroup2Comm());
//                
//                
//                insertPbublicationStat.setString(4, pb.getPumedID());
//                insertPbublicationStat.setString(5, pb.getTypeOfStudy().toUpperCase());
//                insertPbublicationStat.setString(6, pb.getSampleType().toUpperCase());
//                insertPbublicationStat.setString(7, pb.getSampleMatching());
//                insertPbublicationStat.setString(8, pb.getTechnology());
//                insertPbublicationStat.setString(9, pb.getAnalyticalApproach());
//                insertPbublicationStat.setString(10, pb.getEnzyme().toUpperCase());
//                if (pb.getShotgunTargeted() == null) {
//                    pb.setShotgunTargeted("Not Available");
//                }
//                insertPbublicationStat.setString(11, pb.getShotgunTargeted());
//
//                if (pb.getQuantificationBasis() == null) {
//                    pb.setQuantificationBasis("Not Available");
//                }
//                insertPbublicationStat.setString(12, pb.getQuantificationBasis());
//                if (pb.getQuantBasisComment() == null) {
//                    pb.setQuantBasisComment("Not Available");
//                }
//                insertPbublicationStat.setString(13, pb.getQuantBasisComment());
//                insertPbublicationStat.setInt(14, pb.getPatientsGroup1Number());
//                insertPbublicationStat.setInt(15, pb.getPatientsGroup2Number());
//                insertPbublicationStat.setString(16, pb.getNormalizationStrategy());
//                if (pb.getPatientsGroup1() == null) {
//                    pb.setPatientsGroup1("Not Available");
//                }
//                              
//                int z = insertPbublicationStat.executeUpdate();
//                insertPbublicationStat.clearParameters();
//                insertPbublicationStat.close();
//               
//                System.out.println("at index " + z);
//            
//
//            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }

    }

    private void updatPublication() {
        String selectstat = "SELECT  `patient_group_ii` ,  `patient_sub_group_ii` ,  `patient_gr_ii_comment` \n"
                + "FROM  `quant_prot_table` ";

    }

    public Set<QuantProtein> getQuantificationProteins(int dsUnique) {        
        Set<QuantProtein> quantProtList = new HashSet<QuantProtein>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectDsGroupNum = "SELECT `patients_group_i_number` , `patients_group_ii_number` FROM `combined_dataset_table` Where  `index`=?;";
              PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            selectselectDsGroupNumStat.setInt(1, dsUnique+1);
            ResultSet rs = selectselectDsGroupNumStat.executeQuery();
            int groupINum = 0;
            int groupIINum = 0;
            while (rs.next()) {
               groupINum= rs.getInt("patients_group_i_number");
                groupIINum=rs.getInt("patients_group_ii_number");
            }
            rs.close();
            
            
            
            String selectQuantProt = "SELECT * FROM `quantitative_proteins_table`  where `ds_ID` = ?;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProt);
            selectQuantProtStat.setInt(1, dsUnique+1);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantProtein quantProt = new QuantProtein();
                quantProt.setPatientsGroupIINumber(groupIINum);
                quantProt.setPatientsGroupINumber(groupINum);
                quantProt.setProtKey(rs1.getInt("index"));
                quantProt.setDsKey(dsUnique);
                quantProt.setSequance(rs1.getString("sequance"));
                quantProt.setUniprotAccession(rs1.getString("uniprot_accession"));
                quantProt.setUniprotProteinName(rs1.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(rs1.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(rs1.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(rs1.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(rs1.getString("fold_change"));
                quantProtList.add(quantProt);
            }
            rs1.close();
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return quantProtList;

    }

     public Map<String,Set<QuantPeptide>>    getQuantificationPeptides(int dsUnique) {
        Set<QuantPeptide> quantPeptidetList = new HashSet<QuantPeptide>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }            
            String selectQuantPeptides ="SELECT * FROM `quantitative_peptides_table` Where  `DsKey`=?;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantPeptides);
            selectQuantProtStat.setInt(1, dsUnique + 1);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantPeptide quantPeptide = new QuantPeptide();
                quantPeptide.setDsKey(dsUnique);
                quantPeptide.setProtIndex(rs1.getInt("prot_index") - 1);
                quantPeptide.setUniqueId(rs1.getInt("index"));
                quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                quantPeptide.setPeptideSequance(rs1.getString("peptide_sequance"));
                quantPeptide.setModification_comment(rs1.getString("modification_comment"));
                quantPeptide.setString_fc_value(rs1.getString("string_fc_value"));
                quantPeptide.setString_p_value(rs1.getString("string_p_value"));
                quantPeptide.setP_value(rs1.getDouble("p_value"));
                quantPeptide.setRoc_auc(rs1.getDouble("roc_auc"));
                quantPeptide.setFc_value(rs1.getDouble("fc_value"));
                quantPeptide.setP_value_comments(rs1.getString("p_value_comments"));
                quantPeptide.setUniprotProteinAccession(rs1.getString("proteinAccession"));
                quantPeptidetList.add(quantPeptide);
            }
            rs1.close();

            Map<String, Set<QuantPeptide>> quantProtPeptidetList = new HashMap<String, Set<QuantPeptide>>();
            for (QuantPeptide qp : quantPeptidetList) {
                if (!quantProtPeptidetList.containsKey("_"+qp.getUniprotProteinAccession()+"_"+qp.getProtIndex()+"_")) {
                    Set<QuantPeptide> quantPepProtSet = new HashSet<QuantPeptide>();
                    quantProtPeptidetList.put("_"+qp.getUniprotProteinAccession()+"_"+qp.getProtIndex()+"_", quantPepProtSet);

                }
                Set<QuantPeptide> quantPepProtSet = quantProtPeptidetList.get("_"+qp.getUniprotProteinAccession()+"_"+qp.getProtIndex()+"_");
                quantPepProtSet.add(qp);
                quantProtPeptidetList.put("_"+qp.getUniprotProteinAccession()+"_"+qp.getProtIndex()+"_", quantPepProtSet);

            }
            System.gc();
            return quantProtPeptidetList;
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
    }     
     public void    updateQuantificationPeptides(Set<QuantProtein> quantProt) {
         try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectQuantPeptides = "UPDATE `csf_db_v2_1`.`quantitative_peptides_table` SET `proteinAccession` =? WHERE `quantitative_peptides_table`.`prot_index` = ?;";
             for (QuantProtein qp : quantProt) {

                 PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantPeptides);
                 selectQuantProtStat.setString(1, qp.getUniprotAccession());
                 if (qp.getProtKey() == 0) {
                     System.out.println("at zero exist");
                 }
                 selectQuantProtStat.setInt(2, qp.getProtKey());
                 selectQuantProtStat.executeUpdate();

            }
            System.gc();
            conn.close();

        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
        }
      

    }
}
