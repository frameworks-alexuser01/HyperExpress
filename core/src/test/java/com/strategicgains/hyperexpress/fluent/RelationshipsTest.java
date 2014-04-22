package com.strategicgains.hyperexpress.fluent;

import static com.strategicgains.hyperexpress.RelTypes.SELF;
import static com.strategicgains.hyperexpress.RelTypes.UP;
import static com.strategicgains.hyperexpress.fluent.Relationships.namespace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.strategicgains.hyperexpress.domain.Blog;
import com.strategicgains.hyperexpress.domain.Comment;
import com.strategicgains.hyperexpress.domain.Entry;
import com.strategicgains.hyperexpress.domain.Link;

public class RelationshipsTest
{
	@Test
	public void testWithNamespaces()
	throws Exception
	{
		RelationshipBuilder rb =Relationships
			.namespaces(
				namespace("ea", "http://namespaces.example.com/{rel}"),
				namespace("blog", "http://namespaces.example.com/{rel}")
			)

			.forCollectionOf(Blog.class)
				.rel(SELF, "/blogs")

			.forClass(Blog.class)
				.rel("ea:author", "/pi/users/{userId}")
				.rel("blog:entries", "/blogs/{blogId}/entries")
				.rel("self", "/blogs/{blogId}")

			.forCollectionOf(Entry.class)
				.rel(SELF, "/blogs/{blogId}/entries")
				.rel(UP, "/blogs/{blogId}")

			.forClass(Entry.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}")
				.rel("blog:comments", "/blog/{blogId}/entries/{entryId}/comments")
				.rel(UP, "/blogs/{blogId}/entries")

			.forCollectionOf(Comment.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}/comments")
				.rel(UP, "/blogs/{blogId}/entries/{entryId}")
					.title("The parent blog entry")

			.forClass(Comment.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}/comments/{commentId}")
					.title("This very comment")
				.rel(UP, "/blogs/{blogId}/entries/{entryId}")
					.title("The parent blog entry")
				.rel("ea:author", "/pi/users/{userId}")
					.title("The comment author");

		LinkResolver resolver = rb.createResolver()
			.with("blogId", "1234")
			.with("entryId", "5678")
			.with("commentId", "0987")
			.with("userId", "7654");

		verifyNamespacesExist(resolver.getNamespaces());

		List<Link> links = resolver.resolve(Blog.class);
		assertNotNull(links);
		assertEquals(3, links.size());

		links = resolver.resolve(new Blog[0]);
		assertNotNull(links);
		assertEquals(1, links.size());
		assertEquals(SELF, links.get(0).getRel());
		assertEquals("/blogs", links.get(0).getHref());

//		List<Blog> c = new ArrayList<Blog>();
//		c.add(new Blog());
//		links = resolver.resolve(c);
//		assertNotNull(links);
//		assertEquals(1, links.size());
//		assertEquals(SELF, links.get(0).getRel());
//		assertEquals("/blogs", links.get(0).getHref());

		links = resolver.resolve(Entry.class);
		assertNotNull(links);
		assertEquals(3, links.size());

//		links = resolver.resolve(new ArrayList<Entry>());
//		assertNotNull(links);
//		assertEquals(2, links.size());

		links = resolver.resolve(Comment.class);
		assertNotNull(links);
		assertEquals(3, links.size());

//		links = resolver.resolve(new ArrayList<Comment>());
//		assertNotNull(links);
//		assertEquals(3, links.size());
	}

	private void verifyNamespacesExist(Collection<Link> namespaces)
	throws Exception
    {
		assertNotNull(namespaces);
		assertEquals(2, namespaces.size());

		boolean eaChecked = false;
		boolean blogChecked = false;

		for (Link namespace : namespaces)
		{
			if ("ea".equals(namespace.getRel()))
			{
				if (eaChecked) throw new IllegalStateException("Namespace 'ea' already checked.");
				eaChecked = true;
				assertEquals("http://namespaces.example.com/{rel}", namespace.getHref());
			}

			if ("blog".equals(namespace.getRel()))
			{
				if (blogChecked) throw new IllegalStateException("Namespace 'blog' already checked.");
				blogChecked = true;
				assertEquals("http://namespaces.example.com/{rel}", namespace.getHref());
			}
		}

		if (!eaChecked) throw new IllegalStateException("Namespace 'ea' not in returned namespaces");
		if (!blogChecked) throw new IllegalStateException("Namespace 'blog' not in returned namespaces");
    }

	@Test
	public void testWithoutNamespaces()
	{
		RelationshipBuilder rb = Relationships
			.forCollectionOf(Blog.class)
				.rel(SELF, "/blogs")
	
			.forClass(Blog.class)
				.rel("ea:author", "/pi/users/{userId}")
				.rel("blog:entries", "/blogs/{blogId}/entries")
				.rel("self", "/blogs/{blogId}")
	
			.forCollectionOf(Entry.class)
				.rel(SELF, "/blogs/{blogId}/entries")
				.rel(UP, "/blogs/{blogId}")
	
			.forClass(Entry.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}")
				.rel("blog:comments", "/blog/{blogId}/entries/{entryId}/comments")
				.rel(UP, "/blogs/{blogId}/entries")
	
			.forCollectionOf(Comment.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}/comments")
				.rel(UP, "/blogs/{blogId}/entries/{entryId}")
					.title("The parent blog entry")
	
			.forClass(Comment.class)
				.rel(SELF, "/blogs/{blogId}/entries/{entryId}/comments/{commentId}")
				.rel(UP, "/blogs/{blogId}/entries/{entryId}")
					.title("The parent blog entry")
				.rel("ea:author", "/pi/users/{userId}");

		LinkResolver resolver = rb.createResolver()
			.with("blogId", "1234")
			.with("entryId", "5678")
			.with("commentId", "0987")
			.with("userId", "7654");

		List<Link> links = resolver.resolve(Blog.class);
		assertNotNull(links);
		assertEquals(3, links.size());

		links = resolver.resolve(Entry.class);
		assertNotNull(links);
		assertEquals(3, links.size());

		links = resolver.resolve(Comment.class);
		assertNotNull(links);
		assertEquals(3, links.size());
	}
}
