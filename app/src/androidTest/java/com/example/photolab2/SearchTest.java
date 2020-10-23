package com.example.photolab2;

import android.content.Context;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

// Espresso imports

@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class, false, true);

    @Test
    public void searchByTime() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.photolab2", appContext.getPackageName());

        onView(withId(R.id.button_search)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(typeText("testDate1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(click());
        onView(withId(R.id.etToDateTime)).perform(typeText("testDate2"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(click());
        onView(withId(R.id.etKeywords)).perform(typeText("testCaption"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.editText_caption)).check(matches(withText("caption")));
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.button_prev)).perform(click());
    }

//    @Test
//    public void testing() {
//        onView(withId(R.id.button_next)).perform(click());
//        onView(withId(R.id.button_prev)).perform(click());
//        System.out.println("test");
//    }
}

