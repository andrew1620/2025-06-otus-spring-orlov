package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NamedEntityGraph(name = "comments-books-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "book-author-genres-subgraph")},
        subgraphs = {
                @NamedSubgraph(
                        name = "book-author-genres-subgraph",
                        type = Book.class,
                        attributeNodes = {
                                @NamedAttributeNode("author"),
                                @NamedAttributeNode("genres")
                        }
                )
        })
@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
}
