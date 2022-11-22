package com.jitterted.yacht;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("architecture")
public class HexArchTest {

    @Test
    public void applicationMustNotBeAccessedByDomain() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jitterted.yacht");

        var rule = Architectures.layeredArchitecture()
                                .consideringOnlyDependenciesInLayers()
                                .layer("Adapter").definedBy("..adapter..")
                                .layer("Application").definedBy("..application..")
                                .layer("Domain").definedBy("..domain..")
                                .layer("Startup").definedBy("com.jitterted.yacht")

                                .whereLayer("Adapter").mayOnlyBeAccessedByLayers("Startup")
                                .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter", "Startup")
                                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter", "Startup");

        rule.check(importedClasses);
    }
}