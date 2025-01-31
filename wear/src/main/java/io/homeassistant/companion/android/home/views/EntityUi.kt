package io.homeassistant.companion.android.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.home.HomePresenterImpl
import io.homeassistant.companion.android.util.getIcon
import io.homeassistant.companion.android.util.onEntityClickedFeedback
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.util.setChipDefaults

@Composable
fun EntityUi(
    entity: Entity<*>,
    onEntityClicked: (String) -> Unit,
    isHapticEnabled: Boolean,
    isToastEnabled: Boolean
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val attributes = entity.attributes as Map<*, *>
    val iconBitmap = getIcon(attributes["icon"] as String?, entity.entityId.split(".")[0], LocalContext.current)
    val friendlyName = attributes["friendly_name"].toString()

    if (entity.entityId.split(".")[0] in HomePresenterImpl.toggleDomains) {
        ToggleChip(
            checked = entity.state == "on",
            onCheckedChange = {
                onEntityClicked(entity.entityId)
                onEntityClickedFeedback(isToastEnabled, isHapticEnabled, context, friendlyName, haptic)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            appIcon = { Image(asset = iconBitmap ?: CommunityMaterial.Icon.cmd_cellphone) },
            label = {
                Text(
                    text = friendlyName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = entity.state != "unavailable",
            toggleIcon = { ToggleChipDefaults.SwitchIcon(entity.state == "on") },
            colors = ToggleChipDefaults.toggleChipColors(
                checkedStartBackgroundColor = colorResource(id = R.color.colorAccent),
                checkedEndBackgroundColor = colorResource(id = R.color.colorAccent),
                uncheckedStartBackgroundColor = colorResource(id = R.color.colorAccent),
                uncheckedEndBackgroundColor = colorResource(id = R.color.colorAccent),
                checkedContentColor = Color.Black,
                uncheckedContentColor = Color.Black,
                checkedToggleIconTintColor = Color.Yellow,
                uncheckedToggleIconTintColor = Color.DarkGray
            )
        )
    } else {
        Chip(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            icon = { Image(asset = iconBitmap ?: CommunityMaterial.Icon.cmd_cellphone) },
            label = {
                Text(
                    text = friendlyName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = entity.state != "unavailable",
            onClick = {
                onEntityClicked(entity.entityId)
                onEntityClickedFeedback(isToastEnabled, isHapticEnabled, context, friendlyName, haptic)
            },
            colors = setChipDefaults()
        )
    }
}

@Preview
@Composable
private fun PreviewEntityUI() {
    Column {
        EntityUi(
            entity = previewEntity1,
            onEntityClicked = {},
            isHapticEnabled = true,
            isToastEnabled = false
        )
        EntityUi(
            entity = previewEntity2,
            onEntityClicked = {},
            isHapticEnabled = false,
            isToastEnabled = true
        )
    }
}
