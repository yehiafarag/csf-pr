package probe.com.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.dal.DataAccess;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.util.FilesReader;

public class ExperimentModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DataAccess da;
    private FilesReader fr = new FilesReader();

    public ExperimentModel(String url, String dbName, String driver, String userName, String password) {
        da = new DataAccess(url, dbName, driver, userName, password);
    }

    public boolean handelExperiment(File file, String MIMEType, ExperimentBean exp) throws IOException, SQLException {


        exp = fr.readTextFile(file, MIMEType, exp);//method to extract data from proteins files to store them in database       
        boolean test = false;
        if (exp == null)//exp is null
        {
            test = false;
        } else if (exp.getExpFile() == -5) {
            //if new file or no fraction file cancel update 
            if (exp.getExpId() == -1 || exp.getFractionsNumber() == 0)//new exp
            {
                test = false;
            } else {
                test = true;//da.updateFractionRange(exp);
            }

        } else if (exp.getExpFile() == 0)//Protein  file
        {

            if (exp.getExpId() == -1)//new exp
            {
                test = da.setProteinFile(exp);
            } else {
                test = da.updateProteinFile(exp);
            }
        } else if (exp.getExpFile() == -2)//peptide file
        {
            if (exp.getExpId() == -1)//new exp
            {
                test = da.setPeptideFile(exp);
            } else {
                test = da.updatePeptideFile(exp);
            }
        } else if (exp.getExpFile() == -100)//Standard plot file
        {
            if (exp.getExpId() == -1)//new exp
            {
                System.out.println("standard plot prot for new expermint");
                test = da.setStandardPlotProt(exp);//not implemented yet
            } else {
                System.out.println("standard plot prot under processing");
                test = da.updateStandardPlotProt(exp);
            }
        } else //Protein fraction file
        {
            exp.setFractionRange(0);
            if (exp.getExpId() == -1)//new exp
            {
                test = false;//da.setProteinFractionFile(exp);
            } else {
                test = da.updateProteinFractionFile(exp);
            }
        }
        return test;

    }

    public Map<Integer, ExperimentBean> getExperiments() {
        Map<Integer, ExperimentBean> expList = da.getExperiments();
        return expList;
    }

    public ExperimentBean getExperiment(int expId) {
        ExperimentBean exp = da.getExperiment(expId);
        return exp;
    }

    public Map<String, ProteinBean> getProteinsList(int expId) {
        Map<String, ProteinBean> proteinsList = da.getProteinsList(expId);
        return proteinsList;
    }

    public Map<Integer, PeptideBean> getPeptidesList(int expId) {
        Map<Integer, PeptideBean> peptidesList = da.getPeptidesList(expId);
        return peptidesList;
    }

    public Map<Integer, FractionBean> getFractionsList(int expId) {
        Map<Integer, FractionBean> fractionsList = da.getFractionsList(expId);
        return fractionsList;
    }

    ///new v-2
    public Map<Integer, ProteinBean> searchProtein(String accession, int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> protExpList = da.searchProtein(accession, expId, validatedOnly);
        return protExpList;
    }

    public Map<Integer, PeptideBean> getPeptidesProtList(Set<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesProtList(peptideIds);
        return peptidesProtList;
    }

    public Map<Integer, FractionBean> getProteinFractionList(String accession,
            int expId) {
        Map<Integer, FractionBean> protionFractList = da.getProteinFractionList(accession, expId);
        return protionFractList;
    }

    public Map<Integer, ProteinBean> searchProteinByName(String protSearch, int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByName(protSearch, expId, validatedOnly);
        return proteinsList;
    }

    public Map<Integer, ProteinBean> searchProteinByPeptideSequence(String protSearch,
            int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByPeptideSequence(protSearch, expId, validatedOnly);
        return proteinsList;
    }

    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds,
            String accession) {
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(peptideIds, accession);
        return peptidesProtList;
    }

    public Set<Integer> getExpPepProIds(int expId, String accession) {
        Set<Integer> expProPepIds = da.getExpPepProIds(expId, accession);

        return expProPepIds;
    }

    public List<StandardProteinBean> getStandardProtPlotList(int expId) {
        List<StandardProteinBean> standardPlotList = da.getStandardProtPlotList(expId);


        return standardPlotList;
    }

    public boolean updateExpData(ExperimentBean exp) {
        boolean test = da.updateExpData(exp);
        return test;

    }
}