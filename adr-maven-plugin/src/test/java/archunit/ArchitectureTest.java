package archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.ronnyfriedland.adr.template.TemplateProcessor;
import org.apache.maven.plugins.annotations.Mojo;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringOnlyDependenciesInAnyPackage;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;

/**
 * Test architecture rules
 *
 * @author ronnyfriedland
 */
@AnalyzeClasses(packages = "de.ronnyfriedland.adr", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule ruleMojo = classes()
            .that().areAnnotatedWith(Mojo.class)
            .should().haveSimpleNameEndingWith("Mojo");

    @ArchTest
    public static final ArchRule templatePackageRule = classes()
            .that().areAssignableTo(TemplateProcessor.class)
            .should().resideInAPackage("de.ronnyfriedland.adr.template");

    @ArchTest
    public static final ArchRule formatSpecificExporterPackageRule = classes()
            .that().haveSimpleNameStartingWith("Export")
            .and().doNotHaveSimpleName("ExportAdrMojo")
            .and().doNotHaveSimpleName("ExportProcessorException")
            .and().doNotHaveSimpleName("ExportProcessor")
            .should().resideInAPackage("de.ronnyfriedland.adr.export.formats");

    @ArchTest
    public static final ArchRule mojoPackageRule = classes()
            .that().haveSimpleNameEndingWith("Mojo")
            .should().resideInAPackage("de.ronnyfriedland.adr");

    @ArchTest
    public static final ArchRule exceptionsPackageRule = classes()
            .that().areAssignableTo(Exception.class)
            .should().resideInAPackage("..exception");

    @ArchTest
    public static final ArchRule enumsPackageRule = classes()
            .that().areAssignableTo(Enum.class)
            .should().resideInAPackage("..enums");

    @ArchTest
    public static final ArchRule ruleClassDependencies =
            classes()
                    .should(adhereToPlantUmlDiagram("src/main/resources/packages.puml",
                            consideringOnlyDependenciesInAnyPackage("de.ronnyfriedland.adr..")));
}


