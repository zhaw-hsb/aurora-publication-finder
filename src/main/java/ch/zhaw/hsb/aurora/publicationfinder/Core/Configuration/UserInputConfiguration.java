package ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration;

public class UserInputConfiguration {
    
    private static String date;
    
    public static void setDate(String parameterDate){

        date = parameterDate;
    }

    public static String getDate(){

        return date;
    }

}
