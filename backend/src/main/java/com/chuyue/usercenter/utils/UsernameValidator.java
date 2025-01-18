package com.chuyue.usercenter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mkyong
 * @link <a href="https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/"></a>
 */
public class UsernameValidator {

    /**
     ^[a-zA-Z0-9]      # start with an alphanumeric character
     (                 # start of (group 1)
     [._-](?![._-])  # follow by a dot, hyphen, or underscore, negative lookahead to
     # ensures dot, hyphen, and underscore does not appear consecutively
     |               # or
     [a-zA-Z0-9]     # an alphanumeric character
     )                 # end of (group 1)
     {3,18}            # ensures the length of (group 1) between 3 and 18
     [a-zA-Z0-9]$      # end with an alphanumeric character

     # {3,18} plus the first and last alphanumeric characters,
     # total length became {5,20}

     */

    //Strict regex, length between 5-20
    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";

    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    public static boolean isValid(final String username) {
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

}
