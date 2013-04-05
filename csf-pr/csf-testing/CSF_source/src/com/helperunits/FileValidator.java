package com.helperunits;

import java.io.Serializable;

public class FileValidator implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -14218494555136445L;

	public int validateFile(String[] strArr,String MIMEType)//check if the file type and  file format
	{
		
		if(! MIMEType.equalsIgnoreCase("text/plain") ||strArr.length < 8 )//the file type must be 'txt' and the rows greater than 8 (in fraction protein initial rows is 8) 
			return -1;//file is not either text format or headers less than minimum number
		else if(strArr.length == 18 && ( strArr[0].equalsIgnoreCase("Accession") && strArr[12].equalsIgnoreCase("emPAI") && strArr[17].equalsIgnoreCase("Starred")))
		{				
			return 0; //the file is protein file
		}
		else if(strArr.length == 17 && ( strArr[0].equalsIgnoreCase("Protein") && strArr[12].equalsIgnoreCase("Location Confidence") && strArr[16].equalsIgnoreCase("Confidence")))
		{
			return -2;
			
			
		}
		else if(strArr.length >= 8 && strArr[0].equalsIgnoreCase("Accession") && strArr[3].equalsIgnoreCase("Description") && strArr[(strArr.length-1)].equalsIgnoreCase("Starred") )
		{ 
			int fractionNumber =  (strArr.length + 1 - 5)/3; //return the number of the fractions 
			
				return fractionNumber; //for protein   file
			
		}		
		return -1;
	}

}
