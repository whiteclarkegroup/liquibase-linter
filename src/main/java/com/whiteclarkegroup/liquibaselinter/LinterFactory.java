package com.whiteclarkegroup.liquibaselinter;

import com.google.common.collect.ImmutableMap;
import com.whiteclarkegroup.liquibaselinter.linters.*;
import liquibase.change.Change;
import liquibase.change.core.*;

import java.util.Map;
import java.util.Optional;

class LinterFactory {

    private static final Map<Class, Linter> LINTERS =
            ImmutableMap.<Class, Linter>builder()
                    .put(AddColumnChange.class, new AddColumnChangeLinter())
                    .put(CreateTableChange.class, new CreateTableChangeLinter())
                    .put(DeleteDataChange.class, new ModifyDataChangeLinter())
                    .put(UpdateDataChange.class, new ModifyDataChangeLinter())
                    .put(AddUniqueConstraintChange.class, new AddUniqueConstraintChangeLinter())
                    .put(CreateIndexChange.class, new CreateIndexChangeLinter())
                    .put(AddPrimaryKeyChange.class, new AddPrimaryKeyChangeLinter())
                    .put(AddForeignKeyConstraintChange.class, new AddForeignKeyConstraintChangeLinter())
                    .put(RenameTableChange.class, new RenameTableChangeLinter())
                    .put(RenameViewChange.class, new RenameViewChangeLinter())
                    .put(RenameColumnChange.class, new RenameColumnChangeLinter())
                    .put(MergeColumnChange.class, new MergeColumnChangeLinter())
                    .put(DropNotNullConstraintChange.class, new DropNotNullConstraintChangeLinter())
                    .build();

    private LinterFactory() {
    }

    static Optional<Linter> getLinter(final Change change) {
        return Optional.ofNullable(LINTERS.get(change.getClass()));
    }

}
