package com.example.pictures_app.ui.post_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pictures_app.ui.posts.PostsListBody
import com.example.pictures_app.ui.theme.PicturesAppTheme

@Composable
fun PostDetailBody() {
    Column {
        Text("Post Header", style = MaterialTheme.typography.h4)
        Spacer(Modifier.height(16.dp))
        Text("Post Body", style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
fun PostDetailBodyPreview() {
    PicturesAppTheme {
        PostDetailBody()
    }
}