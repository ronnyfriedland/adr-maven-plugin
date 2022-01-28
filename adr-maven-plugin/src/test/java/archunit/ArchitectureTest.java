package archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import de.ronnyfriedland.adr.export.ExportProcessor;
import de.ronnyfriedland.adr.status.StatusProcessor;
import de.ronnyfriedland.adr.template.TemplateProcessor;
import org.apache.maven.plugin.AbstractMojo;
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

    static ArchCondition<JavaClass> notUsedByOtherClasses =
            new ArchCondition<JavaClass>("are not called by other classes") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    if(!item.getMethodCallsToSelf().isEmpty()) {
                        events.add(SimpleConditionEvent.violated(item, "calls to mojo classes are not permitted"));
                    }
                }
            };

    @ArchTest
    public static final ArchRule templatePackageRule = classes()
            .that().areAssignableTo(TemplateProcessor.class)
            .should().resideInAPackage("de.ronnyfriedland.adr.template")
            .andShould().haveSimpleNameEndingWith("Processor");

    @ArchTest
    public static final ArchRule formatSpecificExporterPackageRule = classes()
            .that().haveSimpleNameStartingWith("Export")
                .and().haveSimpleNameEndingWith("Processor")
                .and().doNotHaveSimpleName(ExportProcessor.class.getSimpleName())
            .should().resideInAPackage("de.ronnyfriedland.adr.export.formats");

    @ArchTest
    public static final ArchRule mojoPackageRule = classes()
            .that().areAssignableTo(AbstractMojo.class)
            .should().resideInAPackage("de.ronnyfriedland.adr")
            .andShould().haveSimpleNameEndingWith("Mojo");

    @ArchTest
    public static final ArchRule noCallsToMojos = classes()
            .that().areAnnotatedWith(Mojo.class).and().doNotHaveSimpleName("HelpMojo").should(notUsedByOtherClasses);

    @ArchTest
    public static final ArchRule exceptionsPackageRule = classes()
            .that().areAssignableTo(Exception.class)
            .should().resideInAPackage("..exception")
            .andShould().haveSimpleNameEndingWith("Exception");

    @ArchTest
    public static final ArchRule enumsPackageRule = classes()
            .that().areAssignableTo(Enum.class)
            .should().resideInAPackage("..enums")
            .andShould().haveSimpleNameEndingWith("Type");

    @ArchTest
    public static final ArchRule statusPackageRule = classes()
            .that().haveSimpleName(StatusProcessor.class.getSimpleName())
            .should().resideInAPackage("..status");

    @ArchTest
    public static final ArchRule ruleClassDependencies =
            classes()
                    .should(adhereToPlantUmlDiagram("src/main/resources/packages.puml",
                            consideringOnlyDependenciesInAnyPackage("de.ronnyfriedland.adr..")));
}
