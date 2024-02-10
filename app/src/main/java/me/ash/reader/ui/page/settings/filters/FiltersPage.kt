package me.ash.reader.ui.page.settings.languages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.ash.reader.R
import me.ash.reader.infrastructure.preference.LanguagesPreference
import me.ash.reader.infrastructure.preference.LocalAmoledDarkTheme
import me.ash.reader.infrastructure.preference.LocalFilterDuplicates
import me.ash.reader.infrastructure.preference.LocalLanguages
import me.ash.reader.infrastructure.preference.OpenLinkPreference
import me.ash.reader.infrastructure.preference.not
import me.ash.reader.ui.component.base.Banner
import me.ash.reader.ui.component.base.DisplayText
import me.ash.reader.ui.component.base.FeedbackIconButton
import me.ash.reader.ui.component.base.RYScaffold
import me.ash.reader.ui.component.base.RYSwitch
import me.ash.reader.ui.ext.openURL
import me.ash.reader.ui.page.common.RouteName
import me.ash.reader.ui.page.settings.SettingItem
import me.ash.reader.ui.theme.palette.onLight
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val filterDuplicates = LocalFilterDuplicates.current

    val scope = rememberCoroutineScope()

    RYScaffold(
        containerColor = MaterialTheme.colorScheme.surface onLight MaterialTheme.colorScheme.inverseOnSurface,
        navigationIcon = {
            FeedbackIconButton(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            ) {
                navController.popBackStack()
            }
        },
        content = {
            LazyColumn {
                item {
                    DisplayText(text = stringResource(R.string.filters), desc = "")
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingItem(
                        title = stringResource(R.string.duplicates),
                        desc = stringResource(R.string.filter_duplicate_articles),
                        separatedActions = true,
                        onClick = {
                                  /*
                                  TODO
                                      - "Check only title(s)"
                                      - Compare article content
                                      - Create tiered/sortable list for "preferred" sources
                                   */
//                            navController.navigate(RouteName.DARK_THEME) {
//                                launchSingleTop = true
//                            }
                            (!filterDuplicates).put(context, scope)
                        },
                    ) {
                        RYSwitch(activated = filterDuplicates.value) {
                            (!filterDuplicates).put(context, scope)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingItem(
                        title = stringResource(R.string.add_filter),
                        desc = stringResource(R.string.add_filter_desc),
                        icon = Icons.Outlined.PostAdd,
                        onClick = {
                            // TODO - Launch Dialog for adding filter
                        },
                    ) {}
                }
//                item {
//                    LanguagesPreference.values.map {
//                        SettingItem(
//                            title = it.toDesc(),
//                            onClick = {
//                                it.put(context, scope)
//                            },
//                        ) {
//                            RadioButton(selected = it == languages, onClick = {
//                                it.put(context, scope)
//                            })
//                        }
//                    }
//                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    )
}
