package com.whiteclarkegroup.liquibaselinter;

import com.whiteclarkegroup.liquibaselinter.linters.*;
import liquibase.change.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class LinterFactoryTest {

    @DisplayName("Should return add column linter for add column change type")
    @Test
    void shouldReturnAddColumnLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddColumnChangeLinter.class)
        );
    }

    @DisplayName("Should return create index change linter")
    @Test
    void shouldReturnCreateIndexChangeLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new CreateIndexChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), CreateIndexChangeLinter.class)
        );
    }

    @DisplayName("Should return add unique constraint linter")
    @Test
    void shouldReturnAddUniqueConstraintLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddUniqueConstraintChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddUniqueConstraintChangeLinter.class)
        );
    }

    @DisplayName("Should return add primary key constraint linter")
    @Test
    void shouldReturnAddPrimaryKeyLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddPrimaryKeyChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddPrimaryKeyChangeLinter.class)
        );
    }

    @DisplayName("Should return add foreign key constraint linter")
    @Test
    void shouldReturnAddForeignKeyLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddForeignKeyConstraintChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddForeignKeyConstraintChangeLinter.class)
        );
    }

    @DisplayName("Should return rename view change linter")
    @Test
    void shouldReturnRenameViewLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new RenameViewChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), RenameViewChangeLinter.class)
        );
    }

    @DisplayName("Should return rename column change linter")
    @Test
    void shouldReturnRenameColumnLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new RenameColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), RenameColumnChangeLinter.class)
        );
    }

    @DisplayName("Should return merge column change linter")
    @Test
    void shouldReturnMergeColumnLinter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new MergeColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), MergeColumnChangeLinter.class)
        );
    }
}
