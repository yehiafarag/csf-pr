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

import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
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
                conn_i.close();
            } catch (ClassNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (InstantiationException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (SQLException e) {
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

                //CREATE TABLE `datasets_table`
                String datasets_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (\n" + "  `exp_id` int(11) NOT NULL auto_increment,\n" + "  `fraction_range` int(2) NOT NULL default '0',\n" + "  `name` varchar(100) NOT NULL,\n" + "  `fractions_number` int(11) NOT NULL default '0',\n" + "  `ready` int(11) NOT NULL default '0',\n" + "  `uploaded_by` varchar(100) NOT NULL,\n" + "  `peptide_file` int(2) NOT NULL default '0',\n"
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

                //CREATE TABLE dataset_fractions_table
                String dataset_fractions_table = "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` (  `exp_id` int(11) NOT NULL,`fraction_id` int(11) NOT NULL auto_increment,  `min_range` double NOT NULL default '0',"
                        + "  `max_range` double NOT NULL default '0', `index` int(11) NOT NULL default '0',  PRIMARY KEY  (`fraction_id`),  KEY `exp_id` (`exp_id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(dataset_fractions_table);

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
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(proteins_peptides_table);

                //CREATE TABLE fractions_table
                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (  `fraction_id` int(11) NOT NULL,`prot_accession` varchar(500) NOT NULL,"
                        + "`number_peptides` int(11) NOT NULL default '0',  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,  `number_spectra` int(11) NOT NULL default '0',`average_ precursor_intensity` double default NULL," + "KEY `prot_accession` (`prot_accession`), KEY `fraction_id` (`fraction_id`),	FOREIGN KEY (`prot_accession`) REFERENCES proteins_table(`accession`) ON DELETE CASCADE,"
                        + "FOREIGN KEY (`fraction_id`) REFERENCES experiment_fractions_table(`fraction_id`) ON DELETE CASCADE	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(fractions_table);

                //CREATE TABLE dataset_peptides_proteins_table
                String dataset_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (  `exp_id` varchar(50) NOT NULL,  `peptide_id` int(50) NOT NULL,  `protein` varchar(70) NOT NULL,  UNIQUE KEY `exp_id` (`exp_id`,`peptide_id`,`protein`),  KEY `peptide_id` (`peptide_id`),  KEY `protein` (`protein`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(dataset_peptides_proteins_table);

                //CREATE TABLEstandard_plot_proteins
                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(standard_plot_proteins);
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
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public synchronized boolean setProteinFractionFile(DatasetBean datasetBean) {
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
                for (FractionBean fb : datasetBean.getFractionsList().values()) {
                    insertFractDatasetStat = conn.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
                    insertFractDatasetStat.setInt(1, datasetId);
                    insertFractDatasetStat.setDouble(2, fb.getMinRange());
                    insertFractDatasetStat.setDouble(3, fb.getMaxRange());
                    insertFractDatasetStat.setInt(4, fb.getFractionIndex());
                    insertFractDatasetStat.executeUpdate();
                    rs = insertFractDatasetStat.getGeneratedKeys();
                    while (rs.next()) {
                        fractId = rs.getInt(1);
                    }
                    rs.close();
                    for (ProteinBean pb : fb.getProteinList().values()) {
                        test = this.insertProteinFract(conn, fractId, pb);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (InstantiationException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (SQLException e) {
                System.err.println(e.getLocalizedMessage());
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
            for (ProteinBean pb : fraction.getProteinList().values()) {
                this.insertProteinFract(databaseConnection, fractId, pb);
                this.updateDatasetProteinFraction(pb, datasetId);
            }

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    private synchronized int insertProteinFract(Connection databaseConnection, int fractionId, ProteinBean proteinFraction) {
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
            System.err.println(e.getLocalizedMessage());
            return -1;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

            return -1;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
    public synchronized boolean updateDatasetInformation(Connection databaseConnection, DatasetBean dataset) {
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
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
                for (ProteinBean fpb : fractionBean.getProteinList().values()) {
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
                System.err.println(e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                System.err.println(e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (InstantiationException e) {
                System.err.println(e.getLocalizedMessage());
//                e.printStackTrace();
                return false;
            } catch (SQLException e) {
                System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        return test;
    }

    /**
     * check the availability of a dataset
     *
     * @param datasetId
     * @return dataset if available and null if not
     */
    public synchronized DatasetBean readyToRetrieveDataset(int datasetId) {
        PreparedStatement selectDatasetStat = null;

        String selectDatasetProt = "SELECT * FROM `experiments_table` WHERE `exp_id` = ?";

        DatasetBean dataset = new DatasetBean();

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
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return test;
    }

    // RETRIVEING DATA
    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public synchronized Map<Integer, DatasetBean> getDatasets()//get experiments list
    {
        PreparedStatement selectDatasetListStat = null;
        Map<Integer, DatasetBean> datasetList = new HashMap<Integer, DatasetBean>();
        Map<Integer, DatasetBean> tempDatasetList = new HashMap<Integer, DatasetBean>();
        String selectselectDatasetList = "SELECT * FROM `experiments_table` ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetListStat = conn.prepareStatement(selectselectDatasetList);
            ResultSet rs = selectDatasetListStat.executeQuery();
            while (rs.next()) {
                DatasetBean dataset = new DatasetBean();
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
                DatasetBean dataset = datasetList.get(datasetId);
                List<Integer> fractionIds = this.getFractionIdsList(datasetId);
                dataset.setFractionIds(fractionIds);
                tempDatasetList.put(datasetId, dataset);

            }
            datasetList.clear();
            datasetList.putAll(tempDatasetList);
            tempDatasetList.clear();

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
        System.gc();
        return datasetList;

    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public synchronized DatasetBean getStoredDataset(int datasetId) {
        DatasetBean dataset = new DatasetBean();
        dataset.setDatasetId(datasetId);
        dataset = this.getDatasetDetails(dataset);
        dataset.setFractionsList(this.getFractionsList(dataset.getDatasetId()));
        dataset.setProteinList(this.getDatasetProteinsList(datasetId));	   	//get protein details	
        dataset.setPeptideList(this.getDatasetPeptidesList(datasetId));
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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        return fractionList;
    }

    /**
     * get dataset details
     *
     * @param dataset
     * @return list of fraction Id's list
     */
    private synchronized DatasetBean getDatasetDetails(DatasetBean dataset) {
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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        return dataset;

    }

    /**
     * get dataset fractions list
     *
     * @param datasetId
     * @return fractions list for the selected dataset
     */
    public synchronized Map<Integer, FractionBean> getFractionsList(int datasetId) {
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
            selectFractsListStat.setInt(1, datasetId);
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

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        System.gc();
        return fractionsList;

    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
    @SuppressWarnings("SleepWhileInLoop")
    public synchronized Map<Integer, PeptideBean> getDatasetPeptidesList(int datasetId) {
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
            selectPeptideListStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int peptideId = rs.getInt("pep_id");
                peptideIdList.add(peptideId);

            }
            rs.close();

            //get peptides 
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
                counter++;
                if (counter == 10000) {
                    conn.close();
                    Thread.sleep(100);
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                    counter = 0;
                }

            }

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InterruptedException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        System.gc();
        return peptidesList;
    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public synchronized Map<String, ProteinBean> getDatasetProteinsList(int datasetId) {
        Map<String, ProteinBean> proteinDatasetList = new HashMap<String, ProteinBean>();
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
                    if (temPb.getAccession().equalsIgnoreCase("Q15155")) {
                        System.out.println("temPb.getAccession() + \",\" + temPb.getOtherProteins()  " + temPb.getAccession() + "," + temPb.getOtherProteins());
                    }
                    proteinDatasetList.put(temPb.getAccession() + "," + temPb.getOtherProteins(), temPb);
                }
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
    public synchronized Map<Integer, ProteinBean> searchProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat = null;
        String selectPro = "";
        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id`=? AND  `prot_key` LIKE(?) AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id`=? AND `prot_key` LIKE(?);";
        }

        Map<Integer, ProteinBean> protDatasetList = new HashMap<Integer, ProteinBean>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat.setString(2, "%" + accession + "%");
            selectProStat.setInt(1, datasetId);
            if (validatedOnly) {
                selectProStat.setString(3, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            while (rs.next()) {
                ProteinBean temPb = new ProteinBean();
                temPb.setDatasetId(datasetId);
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
                protDatasetList.put(temPb.getProtGroupId(), temPb);
            }
            rs.close();
            System.gc();
            return protDatasetList;

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * get peptides list for giving ids
     *
     * @param peptideIds peptides IDs
     * @return peptides list
     */
    public synchronized Map<Integer, PeptideBean> getPeptidesList(Set<Integer> peptideIds) {

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
                    pepb.setValidated(rs.getDouble("validated"));
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

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

        }
        System.gc();
        return peptidesList;
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

        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
    public synchronized Map<Integer, ProteinBean> searchProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
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
            selectProStat.setString(1, "%" + protSearchKeyword + "%");
            selectProStat.setInt(2, datasetId);
            if (validatedOnly) {
                selectProStat.setString(3, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            int in = 1;
            while (rs.next()) {
                ProteinBean temPb = new ProteinBean();
                temPb.setDatasetId(datasetId);
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
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
    public synchronized Map<Integer, ProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {
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
                            Map<Integer, ProteinBean> tempProteinsList = this.searchProteinByAccession(str.trim(), datasetId, validatedOnly);
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
            System.err.println(e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public boolean updateFractionRange(DatasetBean dataset) {
        List<Integer> fractionIDs = this.getFractionIdsList(dataset.getDatasetId());
        java.util.Collections.sort(fractionIDs);
        Map<Integer, FractionBean> fractionRangeList = dataset.getFractionsList();
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
            } catch (ClassNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (InstantiationException e) {
                System.err.println(e.getLocalizedMessage());
                return false;
            } catch (SQLException e) {
                System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public synchronized boolean insertFractions(Connection connection, DatasetBean dataset) {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(driver).newInstance();
                connection = DriverManager.getConnection(url + dbName, userName, password);
            }

            String insertFractDataset = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            PreparedStatement insertFractDatasetStat = conn.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
            int fractId = 0;

            for (FractionBean fb : dataset.getFractionsList().values()) {
                insertFractDatasetStat = connection.prepareStatement(insertFractDataset, Statement.RETURN_GENERATED_KEYS);
                insertFractDatasetStat.setInt(1, dataset.getDatasetId());
                insertFractDatasetStat.setDouble(2, fb.getMinRange());
                insertFractDatasetStat.setDouble(3, fb.getMaxRange());
                insertFractDatasetStat.setInt(4, fb.getFractionIndex());
                insertFractDatasetStat.executeUpdate();
                ResultSet rs = insertFractDatasetStat.getGeneratedKeys();
                while (rs.next()) {
                    fractId = rs.getInt(1);
                }
                for (ProteinBean pb : fb.getProteinList().values()) {
                    this.insertProteinFract(connection, fractId, pb);
                }
                rs.close();
            }

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
    public synchronized boolean updateProtFractionFile(DatasetBean tempDataset, DatasetBean dataset) {
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
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public boolean updateDatasetProteinFraction(ProteinBean proteinBean, int datasetId) {

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
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            test = false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public int insertDatasetProteinPeptide(int datasetId, int peptideId, String accession, Connection connection) {
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
    public boolean updateStandardPlotProt(DatasetBean dataset) {
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
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
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
     * @return standardPlotList
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
    public boolean updateDatasetData(DatasetBean dataset) {

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
            System.err.println(e.getLocalizedMessage());
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
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        }

        return false;

    }

    /**
     * store peptides data for dataset in the database
     *
     *
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

}
