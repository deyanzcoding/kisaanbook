package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.PostType
import com.example.ui.theme.ThemeMode
import com.example.viewmodel.AppTab
import com.example.viewmodel.KisaanBhaiViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kisaan Book", appName)
  }

  @Test
  fun `viewmodel initial state and tab switching`() {
    val viewModel = KisaanBhaiViewModel()
    assertEquals(AppTab.HOME, viewModel.currentTab.value)
    assertTrue(viewModel.posts.value.isNotEmpty())
    assertTrue(viewModel.questions.value.isNotEmpty())
    assertTrue(viewModel.listings.value.isNotEmpty())

    viewModel.setTab(AppTab.COMMUNITY)
    assertEquals(AppTab.COMMUNITY, viewModel.currentTab.value)
  }

  @Test
  fun `theme switcher updates theme mode`() {
    val viewModel = KisaanBhaiViewModel()
    assertEquals(ThemeMode.SYSTEM, viewModel.themeMode.value)

    viewModel.setThemeMode(ThemeMode.DARK)
    assertEquals(ThemeMode.DARK, viewModel.themeMode.value)

    viewModel.setThemeMode(ThemeMode.LIGHT)
    assertEquals(ThemeMode.LIGHT, viewModel.themeMode.value)
  }

  @Test
  fun `language switcher updates language and strings`() {
    val viewModel = KisaanBhaiViewModel()
    assertEquals(AppLanguage.ENGLISH, viewModel.appLanguage.value)
    assertEquals("Kisaan Book", Strings.appName(AppLanguage.ENGLISH))

    viewModel.setLanguage(AppLanguage.URDU)
    assertEquals(AppLanguage.URDU, viewModel.appLanguage.value)
    assertEquals("کسان بک", Strings.appName(AppLanguage.URDU))

    viewModel.setLanguage(AppLanguage.PASHTO)
    assertEquals(AppLanguage.PASHTO, viewModel.appLanguage.value)
    assertEquals("کِسان بُک", Strings.appName(AppLanguage.PASHTO))
  }

  @Test
  fun `auth login and signup flow`() {
    val viewModel = KisaanBhaiViewModel()
    assertTrue(viewModel.isLoggedIn.value)

    viewModel.logout()
    assertFalse(viewModel.isLoggedIn.value)

    viewModel.login("0300-1234567", "1234")
    assertTrue(viewModel.isLoggedIn.value)

    viewModel.signup(
      name = "Gul Khan",
      phone = "0333-9876543",
      district = "Swat",
      cropsStr = "Maize, Apples",
      pin = "5678"
    )
    assertTrue(viewModel.isLoggedIn.value)
    assertEquals("Gul Khan", viewModel.currentUser.value.name)
    assertEquals("Swat", viewModel.selectedDistrict.value)
  }

  @Test
  fun `toggle like and add comment flow`() {
    val viewModel = KisaanBhaiViewModel()
    val initialPost = viewModel.posts.value.first()
    val initialLikes = initialPost.likeCount

    viewModel.toggleLike(initialPost.id)
    val updatedPost = viewModel.posts.value.first { it.id == initialPost.id }
    assertTrue(updatedPost.isLiked)
    assertEquals(initialLikes + 1, updatedPost.likeCount)

    viewModel.addComment(initialPost.id, "Very useful advice for yellow rust!")
    val postWithComment = viewModel.posts.value.first { it.id == initialPost.id }
    assertEquals(initialPost.commentCount + 1, postWithComment.commentCount)
  }

  @Test
  fun `create new farm post flow`() {
    val viewModel = KisaanBhaiViewModel()
    val initialCount = viewModel.posts.value.size

    viewModel.createPost(
      text = "Harvesting Basmati 515 rice today in Swat command area.",
      category = "Rice",
      postType = PostType.GENERAL,
      district = "Swat",
      imageRes = null
    )

    assertEquals(initialCount + 1, viewModel.posts.value.size)
    assertEquals("Rice", viewModel.posts.value.first().category)
  }
}
