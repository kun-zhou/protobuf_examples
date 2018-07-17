package me.kunzhou.protobuf_examples.oneof;

import com.google.protobuf.NullValue;


public class App {

    // use oneof to implement nullability

    public static void main(String[] args) {
        //*** 1. play around with a NullableString ***//
         System.out.println(">>> 1. playaround with a NullableString");
        NullableString nullableString = NullableString.newBuilder().setStringValue("Heys").build();
        
        if (nullableString.getStringCase() != NullableString.StringCase.STRING_NOT_SET) {
            System.out.println("Field set!!");
        } else {
            System.out.println("Field NOT set!!");
        }
        if (nullableString.getStringCase() == NullableString.StringCase.STRINGVALUE) {
            System.out.println("We have String value!!!");
        }
        if (nullableString.getStringCase() == NullableString.StringCase.NULLVALUE) {
            System.out.println("We have Null value!!!");
        }
        
        //*** 2. usage with the toString and fromString method
        System.out.println(">>> 2. usage with the fromString and toString method");
        System.out.println(toString(nullableString));
        System.out.println(toString(fromString("Hello World")));
        System.out.println(toString(fromString(null)));
    }

    public static NullableString fromString(String string) {
        if (string == null) {
            return NullableString.newBuilder().setNullValue(NullValue.NULL_VALUE).build();
        }
        return NullableString.newBuilder().setStringValue(string).build();
    }

    public static String toString(NullableString nullableString) {
        NullableString.StringCase strinCase = nullableString.getStringCase();
        if (strinCase == NullableString.StringCase.STRINGVALUE) {
            return nullableString.getStringValue();
        } 
        if (strinCase == NullableString.StringCase.NULLVALUE){
            return null;
        }
        throw new RuntimeException("NullableString is unset");
    }
}

