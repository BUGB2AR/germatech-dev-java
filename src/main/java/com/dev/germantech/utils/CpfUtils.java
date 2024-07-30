package com.dev.germantech.utils;

public class CpfUtils {

    public static boolean isValidCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < 9; i++) {
            sum1 += (10 - i) * (cpf.charAt(i) - '0');
        }

        int remainder1 = sum1 % 11;
        int digit1 = remainder1 < 2 ? 0 : 11 - remainder1;

        if (digit1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        for (int i = 0; i < 10; i++) {
            sum2 += (11 - i) * (cpf.charAt(i) - '0');
        }

        int remainder2 = sum2 % 11;
        int digit2 = remainder2 < 2 ? 0 : 11 - remainder2;

        return digit2 == (cpf.charAt(10) - '0');
    }
}
