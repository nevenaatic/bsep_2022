package com.example.demo.service;

public class RandomString {
	  
    // function to generate a random string of length n
    static String getAlphaNumericString()
    {
  
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(8);
  
        for (int i = 0; i < 8; i++) {
  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }
  
    public static void main(String[] args)
    {
        // Get and display the alphanumeric string
        System.out.println(RandomString
                               .getAlphaNumericString());
    }
}