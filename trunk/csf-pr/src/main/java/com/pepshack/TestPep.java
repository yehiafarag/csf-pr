/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

/**
 *
 * @author Yehia Mokhtar
 */
import java.io.File;
//import net.lingala.zip4j.core.ZipFile;
//import net.lingala.zip4j.exception.ZipException;
public class TestPep {
    
private String folder="";
	private File cpsFile;
	private PSFileImporter fileImporter ;
	private DataHandler dataHandler = new DataHandler();
        	public TestPep(File resourcefolder)
	{
		try {
                    
                    
			
			//int z=(int) ((Integer)1000*Math.random());
			
			//folder =file.getAbsolutePath().substring(0,(file.getAbsolutePath().length()-4))+"_"+z;
			///System.out.println(folder);
			//File dir = new File(folder);
			//dir.mkdir();
			// Initiate ZipFile object with the path/name of the zip file.
			///ZipFile zipFile = new ZipFile(file);
			
			// Extracts all files to the path specified
			//zipFile.extractAll(dir.getAbsolutePath());
			File[] files = resourcefolder.listFiles();
			for(File f:files)
				{
                                    if(f.isDirectory())
                                    {
                                       File[] files2 = f.listFiles(); 
                                       for(File cps:files2)
                                       {
                                        if(cps.getName().contains("cps")){
						cpsFile = cps;
						break;
					}
                                       }
                                    }
					
				}
			System.out.println(cpsFile.getName());
			fileImporter = new PSFileImporter();
			fileImporter.importPeptideShakerFile(cpsFile, "D:/csf-pr-psr/csf-pr");
			dataHandler.handelData(fileImporter);
			
			
			
			//System.out.println(psGui.getProteinStructurePanel().getProteinTable().getValueAt(1, 1));
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		//File cpsFile = new File("2");
	//	
	//	psGui.setVisible(false);
	}
	public TestPep(File file, String rec)
	{
		try {
                    
                    
			
			int z=(int) ((Integer)1000*Math.random());
			
			folder =file.getAbsolutePath().substring(0,(file.getAbsolutePath().length()-4))+"_"+z;
			System.out.println(folder);
			File dir = new File(folder);
			dir.mkdir();
			// Initiate ZipFile object with the path/name of the zip file.
		//	ZipFile zipFile = new ZipFile(file);
			
			// Extracts all files to the path specified
		//	zipFile.extractAll(dir.getAbsolutePath());
			File[] files = dir.listFiles();
			for(File f:files)
				{
					if(f.getName().contains("cps")){
						cpsFile = f;
						break;
					}
				}
			System.out.println(cpsFile.getName());
			fileImporter = new PSFileImporter();
			fileImporter.importPeptideShakerFile(cpsFile, rec);
                        System.out.println("start reading work");
			dataHandler.handelData(fileImporter);
			 System.exit(0);
			
			
			//System.out.println(psGui.getProteinStructurePanel().getProteinTable().getValueAt(1, 1));
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		//File cpsFile = new File("2");
	//	
	//	psGui.setVisible(false);
	}
        
        public static void  main(String[] arg)
        {
            File f = new File("C:/Users/Yehia Mokhtar/Desktop/koko.zip");
            System.out.println(f.isFile());
            File folder = new File("D:/csf-pr-psr/csf-pr/resources");
            System.out.println(folder.exists());
            TestPep tp = new TestPep(folder);
        }
}
