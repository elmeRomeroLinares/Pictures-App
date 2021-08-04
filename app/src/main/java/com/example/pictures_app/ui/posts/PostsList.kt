package com.example.pictures_app.ui.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.ui.theme.PicturesAppTheme

val post1 = PostModel(1,1,
    "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
    "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto")

val post2 = PostModel(1,2,
    "qui est esse",
    "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla")
val postList = listOf(post1, post2)

@Composable
fun PostsListBody(posts: List<PostModel>?, onClick: (PostModel) -> Unit) {
    val scrollState = rememberLazyListState()
    if (!posts.isNullOrEmpty()) {
        LazyColumn(state = scrollState) {
            items(items = posts) { post ->
                PostsListItem(post = post, Modifier.fillMaxWidth(), onClick = onClick)
            }
        }
    } else {
        Text("An Error Occurred While Getting Posts")
    }
}

@Composable
fun PostsListItem(post: PostModel, modifier: Modifier = Modifier, onClick: (PostModel) -> Unit) {
    Column(
        modifier = modifier
            .clickable { onClick(post) }
    ) {
        Text(
            post.postTitle,
            style = MaterialTheme.typography.h5,
            modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
            maxLines = 1
        )
        Text(
            post.postBody,
            fontStyle = FontStyle.Italic,
            maxLines = 2
        )
        Spacer(Modifier.height(8.dp))
        Divider(color = Color.Black)
    }
}

@Preview
@Composable
fun PostsListBodyPreview() {
    PicturesAppTheme() {
        PostsListBody(posts = postList, onClick = {})
    }
}
