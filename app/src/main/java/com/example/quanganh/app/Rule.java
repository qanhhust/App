package com.example.quanganh.app;

/**
 * Created by QuangAnh on 5/10/2017.
 */

public abstract class Rule {

    public String vowel = "aáàảãạ" + "ăắằẳẵặ" + "âấầẩẫậ" + "eéèẻẽẹ"
            + "êếềễểệ" + "iíìỉĩị" + "oóòỏõọ" + "ôốồổỗộ"
            + "ơớờởỡợ" + "uúùủũụ" + "ưứừửữự" + "yýỳỷỹỵ";
    public String consonant = "bcdđghklmnpqrstvx";

    public abstract boolean checkValid(String str);

    public abstract void show();

}

class Rule1 extends Rule {

    @Override
    public boolean checkValid(String str) {
        for (int i = 0; i < str.length() - 3; ++i) {
            if ((vowel.contains(str.charAt(i) + "") && consonant.contains(str.charAt(i + 1) + "") && vowel.contains(str.charAt(i + 2) + ""))
                    || (vowel.contains(str.charAt(i + 1) + "") && consonant.contains(str.charAt(i + 2) + "") && vowel.contains(str.charAt(i + 3) + ""))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 1");
    }

}

class Rule2 extends Rule {

    @Override
    public boolean checkValid(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (consonant.contains(str.charAt(i) + "")) {
                count++;
            }
        }
        return count <= 5;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 2");
    }

}

class Rule3 extends Rule {

    @Override
    public boolean checkValid(String str) {
        int length = str.length();
        if (length > 1 && str.charAt(0) == 'c') {
            if (consonant.contains(str.charAt(1) + "") && str.charAt(1) != 'h') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 3");
    }

}

class Rule4 extends Rule {

    @Override
    public boolean checkValid(String str) {
        int length = str.length();
        if (length > 1) {
            for (int i = 0; i < length - 1; ++i) {
                if (consonant.contains(str.charAt(i) + "") && consonant.contains(str.charAt(i + 1) + "")) {
                    switch (str.charAt(i)) {
                        case 'b':
                        case 'd':
                        case 'đ':
                        case 'h':
                        case 'l':
                        case 'm':
                        case 'q':
                        case 'r':
                        case 's':
                        case 'v':
                        case 'x':
                            return false;
                        case 'p':
                        case 'g':
                        case 'k':
                        case 'c':
                            if (str.charAt(i + 1) != 'h') {
                                return false;
                            }
                            break;
                        case 't':
                            if (str.charAt(i + 1) != 'r' && str.charAt(i + 1) != 'h') {
                                return false;
                            }
                            break;
                        case 'n':
                            if (str.charAt(i + 1) != 'h' && str.charAt(i + 1) != 'g') {
                                return false;
                            }
                            break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 4");
    }

}

class Rule5 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 2) {
            for (int i = 0; i < str.length() - 2; ++i) {
                if (consonant.contains(str.charAt(i) + "") && consonant.contains(str.charAt(i + 1) + "") && consonant.contains(str.charAt(i + 2) + "")) {
                    if (str.charAt(i) != 'n' || str.charAt(i + 1) != 'g' || str.charAt(i + 2) != 'h') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 5");
    }

}

class Rule6 extends Rule {

    @Override
    public boolean checkValid(String str) {
        String before = "áạ" + "ắặ" + "ấậ" + "éẹ"
                + "ếệ" + "íị" + "úụ" + "ứự"
                + "óọ" + "ốộ" + "ớợ" + "ýỵ";
        if (str.length() > 1) {
            for (int i = 1; i < str.length(); ++i) {
                switch (str.charAt(i)) {
                    case 't':
                    case 'c':
                    case 'p':
                        if (vowel.contains(str.charAt(i - 1) + "") && !before.contains(str.charAt(i - 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        if (str.length() > 2) {
            for (int i = 1; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'c' && str.charAt(i + 1) == 'h') {
                    if (vowel.contains(str.charAt(i - 1) + "") && !before.contains(str.charAt(i - 1) + "")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 6");
    }

}

class Rule7 extends Rule {

    @Override
    public boolean checkValid(String str) {
        String con = "qvbdlksxrđ";
        if (str.length() > 1) {
            if (con.contains(str.charAt(str.length() - 1) + "")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 7");
    }

}

class Rule8 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            if (consonant.contains(str.charAt(str.length() - 1) + "")) {
                switch (str.charAt(str.length() - 1)) {
                    case 'h':
                        if (consonant.contains(str.charAt(str.length() - 2) + "")
                                && str.charAt(str.length() - 2) != 'n'
                                && str.charAt(str.length() - 2) != 'c') {
                            return false;
                        }
                        break;
                    case 'g':
                        if (consonant.contains(str.charAt(str.length() - 2) + "")
                                && str.charAt(str.length() - 2) != 'n') {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 8");
    }

}

class Rule9 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            int lastIndex = str.length() - 1;
            if (consonant.contains(str.charAt(lastIndex) + "")) {
                switch (str.charAt(lastIndex)) {
                    case 'n':
                    case 'm':
                    case 't':
                    case 'p':
                    case 'c':
                        if (consonant.contains(str.charAt(lastIndex - 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 9");
    }

}

class Rule10 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            String vow = "aăâeêioôơuưy";
            for (int i = 0; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'a') {
                    if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 10");
    }

}

class Rule11 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            String vow = "iuoy";
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'á':
                    case 'à':
                        if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 11");
    }

}

class Rule12 extends Rule {

    @Override
    public boolean checkValid(String str) {
        Rule13 r = new Rule13();
        if (r.checkValid(str)) {
            return true;
        }
        if (str.length() > 1) {
            String vow = "ioy";
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'ã':
                    case 'ạ':
                    case 'ả':
                        if (!vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 12");
    }

}

class Rule13 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() != 4 && str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'ả' && str.charAt(i + 1) == 'u') {
                    return false;
                }
            }
        } else if (str.length() == 4) {
            if (str.charAt(2) == 'ả' && str.charAt(3) == 'u') {
                if (str.charAt(0) != 'n' || str.charAt(1) != 'h') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 13");
    }

}

class Rule14 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            String vow = "uy";
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'â':
                    case 'ấ':
                    case 'ẩ':
                    case 'ẫ':
                    case 'ậ':
                    case 'ầ':
                        if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 14");
    }

}

class Rule15 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'ă':
                    case 'ắ':
                    case 'ằ':
                    case 'ẳ':
                    case 'ẵ':
                    case 'ặ':
                        if (vowel.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 15");
    }

}

class Rule16 extends Rule {

    @Override
    public boolean checkValid(String str) {
        Rule17 r = new Rule17();
        if (r.checkValid(str)) {
            return true;
        }
        if (str.length() > 1) {
            String vow = "auêếệểễềeẻèẽẹé";
            for (int i = 0; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'i') {
                    if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 16");
    }

}

class Rule17 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() != 3 && str.length() != 4 && str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'i' && str.charAt(i + 1) == 'ữ') {
                    return false;
                }
            }
        } else if (str.length() == 3) {
            if (str.charAt(1) == 'i' && str.charAt(2) == 'ữ') {
                if (str.charAt(0) != 'g') {
                    return false;
                }
            }
        } else if (str.length() == 4) {
            if (str.charAt(1) == 'i' && str.charAt(2) == 'ữ' && str.charAt(3) == 'a') {
                if (str.charAt(0) != 'g') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 17");
    }

}

class Rule18 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 18");
    }

}

class Rule19 extends Rule {

    @Override
    public boolean checkValid(String str) {
        String vow = "ua";
        if (str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'ì':
                    case 'ỉ':
                    case 'ị':
                    case 'ĩ':
                    case 'í':
                        if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 19");
    }

}

class Rule20 extends Rule {

    @Override
    public boolean checkValid(String str) {
        String vow = "o";
        if (str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'e':
                    case 'é':
                    case 'è':
                    case 'ẻ':
                    case 'ẽ':
                    case 'ẹ':
                        if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 20");
    }

}

class Rule21 extends Rule {

    @Override
    public boolean checkValid(String str) {
        Rule22 r22 = new Rule22();
        if (r22.checkValid(str)) {
            return true;
        }
        if (str.length() > 1) {
            String vow = "u";
            for (int i = 0; i < str.length() - 1; ++i) {
                switch (str.charAt(i)) {
                    case 'ê':
                    case 'ế':
                    case 'ề':
                    case 'ệ':
                    case 'ể':
                        if (!vow.contains(str.charAt(i + 1) + "")) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 21");
    }

}

class Rule22 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() != 3 && str.length() != 4 && str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                if (str.charAt(i) == 'ễ' && str.charAt(i + 1) == 'u') {
                    return false;
                }
            }
        } else if (str.length() == 3) {
            if (str.charAt(1) == 'ễ' && str.charAt(2) == 'u') {
                if (str.charAt(0) != 't') {
                    return false;
                }
            }
        } else if (str.length() == 4) {
            if (str.charAt(1) == 'ễ' && str.charAt(2) == 'u') {
                if (str.charAt(0) != 'p' || str.charAt(1) != 'h') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 22");
    }

}

class Rule23 extends Rule {

    @Override
    public boolean checkValid(String str) {
        Rule24 rule24 = new Rule24();
        if (rule24.checkValid(str)) {
            return true;
        }
        if (str.length() > 1) {
            String vow = "aàáảãạăắằẳẵặiíìỉĩịeéèẻẽẹ";
            for (int i = 0; i < str.length() - 1; ++i) {
                if (vowel.contains(str.charAt(i) + "") && str.charAt(i) == 'o') {
                    if (vowel.contains(str.charAt(i + 1) + "") && !vow.contains(str.charAt(i + 1) + "")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 23");
    }

}

class Rule24 extends Rule {

    @Override
    public boolean checkValid(String str) {
        if (str.length() != 5 && str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                if (vowel.contains(str.charAt(i) + "")
                        && str.charAt(i) == 'o'
                        && str.charAt(i + 1) == 'o') {
                    return false;
                }
            }
        } else if (str.length() == 5) {
            String first = "cxb";
            if (str.charAt(1) == 'o' && str.charAt(2) == 'o'
                    && str.charAt(3) == 'n' && str.charAt(4) == 'g') {
                if (!first.contains(str.charAt(0) + "")) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 24");
    }

}

class Rule25 extends Rule {

    @Override
    public boolean checkValid(String str) {
        String vow = "aie";
        if (str.length() > 1) {
            for (int i = 0; i < str.length() - 1; ++i) {
                if (vowel.contains(str.charAt(i) + "")) {
                    switch (str.charAt(i)) {
                        case 'ò':
                        case 'ó':
                        case 'ỏ':
                        case 'ọ':
                        case 'õ':
                            if (vowel.contains(str.charAt(i + 1) + "")
                                    && !vow.contains(str.charAt(i + 1) + "")) {
                                return false;
                            }
                            break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 25");
    }

}

class Rule26 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 26");
    }

}

class Rule27 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 27");
    }

}

class Rule28 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 28");
    }

}

class Rule29 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 29");
    }

}

class Rule30 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 33");
    }

}

class Rule31 extends Rule {

    @Override
    public boolean checkValid(String str) {

        return true;
    }

    @Override
    public void show() {
        System.err.println("Sai luat 31");
    }

}