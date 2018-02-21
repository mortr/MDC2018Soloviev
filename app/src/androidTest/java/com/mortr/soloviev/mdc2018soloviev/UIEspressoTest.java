package com.mortr.soloviev.mdc2018soloviev;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomePagesActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UIEspressoTest {

    private final Context context = InstrumentationRegistry.getTargetContext();
    @Rule
    public ActivityTestRule<WelcomePagesActivity> activityRule = new ActivityTestRule<>(WelcomePagesActivity.class);

    @Test
    public void WelcomeActivityNextButtonExistsTest() {
        onView(withId(R.id.nextButton)).check(matches(isDisplayed()));
    }

    @Test
    public void TrackFromWelcomeActivityTest() {
        onView(withId(R.id.nextButton)).perform(click());
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
    }

    @Test
    public void WelcomeActivityCompactLayoutChooserTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.compact_layout_frame)).perform(click());
        assertEquals(false, Utils.isStandardLayoutsWasSaved(context));
    }

    @Test
    public void WelcomeActivityCompactLayoutChooserSelectRadioTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.compact_layout_frame)).perform(click());
        onView(withId(R.id.compact_layout_radio)).check(matches(isChecked()));
    }

    @Test
    public void WelcomeActivityStandartLayoutChooserTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.standard_layout_frame)).perform(click());
        assertEquals(true, Utils.isStandardLayoutsWasSaved(context));
    }

    @Test
    public void WelcomeActivityStandartLayoutChooserSelectRadioTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.standard_layout_frame)).perform(click());
        onView(withId(R.id.standard_layout_radio)).check(matches(isChecked()));
    }

    @Test
    public void WelcomeActivityBlackThemeChooserTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.black_theme_radio_frame)).perform(click());
        assertEquals(false, Utils.isWhiteTheme(context));
    }


    @Test
    public void WelcomeActivityBlackThemeChooserSelectRadioTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.black_theme_radio_frame)).perform(click());
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.black_theme_radio)).check(matches(isChecked()));
    }

    @Test
    public void WelcomeActivityWhiteThemeChooserTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.white_theme_radio_frame)).perform(click());
        assertEquals(true, Utils.isWhiteTheme(context));
    }

    @Test
    public void WelcomeActivityWhiteThemeChooserSelectRadioTest() {
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.white_theme_radio_frame)).perform(click());
        onView(withId(R.id.view_pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.white_theme_radio)).check(matches(isChecked()));
    }
}
