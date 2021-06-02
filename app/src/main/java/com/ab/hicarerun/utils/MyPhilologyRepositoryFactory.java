//package com.ab.hicarerun.utils;
//
//import androidx.annotation.Nullable;
//
//import com.jcminarro.philology.PhilologyRepository;
//import com.jcminarro.philology.PhilologyRepositoryFactory;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Locale;
//
///**
// * Created by Arjun Bhatt on 3/18/2020.
// */
//public class MyPhilologyRepositoryFactory implements PhilologyRepositoryFactory {
//    @Nullable
//    @Override
//    public PhilologyRepository getPhilologyRepository(@NotNull Locale locale) {
//        if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
//            return new EnglishPhilologyRepository();
//        }
//        return null;
//    }
//}