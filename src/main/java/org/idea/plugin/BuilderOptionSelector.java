package org.idea.plugin;

import java.util.Collections;
import java.util.List;
import javax.swing.*;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.NonFocusableCheckBox;
import org.jetbrains.annotations.NotNull;

public final class BuilderOptionSelector
{

    public static List<PsiFieldMember> selectFieldsAndOptions(final List<PsiFieldMember> members,
                                                              final Project project)
    {
        if (members == null || members.isEmpty())
        {
            return null;
        }


        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        final JCheckBox[] optionCheckBoxes = buildCheckBoxes(propertiesComponent);

        final PsiFieldMember[] memberArray = members.toArray(new PsiFieldMember[members.size()]);

        final MemberChooser<PsiFieldMember> chooser = new MemberChooser<PsiFieldMember>(memberArray,
                false, // allowEmptySelection
                true,  // allowMultiSelection
                project, null, optionCheckBoxes);

        chooser.setTitle("Select Fields and Options for the Builder");
        chooser.selectElements(memberArray);
        if (chooser.showAndGet())
        {
            return chooser.getSelectedElements();
        }

        return Collections.emptyList();
    }


    private static JCheckBox[] buildCheckBoxes(final PropertiesComponent propertiesComponent)
    {

        final JCheckBox[] checkBoxesArray = new JCheckBox[1];
        checkBoxesArray[0] = buildJacksonCheckbox(propertiesComponent);
        return checkBoxesArray;
    }


    @NotNull
    private static JCheckBox buildJacksonCheckbox(final PropertiesComponent propertiesComponent)
    {
        final JCheckBox optionCheckBox = new NonFocusableCheckBox("Add Jackson annotation");
        optionCheckBox.setMnemonic('j');
        optionCheckBox.setToolTipText("Jackson annotation will be added in constructor and getters");

        final String property = BuilderOption.IS_JACKSON_ENABLED.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;
    }
}

