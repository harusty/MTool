package com.zzkx.mtool.util;

import android.text.TextUtils;

import com.hyphenate.util.HanziToPinyin;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/12/3.
 */

public class InitialUtil {
    public static String getInitial(String name) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if (!TextUtils.isEmpty(name)) {
            letter = new GetInitialLetter().getLetter(name);
        }
        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(name)) {
            letter = new GetInitialLetter().getLetter(name);
        }
        return letter;
    }
}
