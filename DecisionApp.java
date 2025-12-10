package view;

import model.Decision;
import model.DecisionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class DecisionApp extends JFrame {
    private final DecisionManager manager;

    private final JTextField titleField = new JTextField(20);
    private final JTextField descField = new JTextField(20);
    private final JTextField urgencyField = new JTextField(3);
    private final JTextField importanceField = new JTextField(3);
    private final JTextField valueField = new JTextField(3);

    private final DefaultListModel<Decision> listModel = new DefaultListModel<>();
    private final JList<Decision> decisionJList = new JList<>(listModel);

    public DecisionApp(DecisionManager manager) {
        this.manager = manager;
        setTitle("Daily Decision Helper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        initLayout();
    }

    private void initLayout() {
        JPanel main = new JPanel(new BorderLayout(6,6));

        JPanel input = new JPanel(new FlowLayout(FlowLayout.LEFT));
        input.add(new JLabel("Title:"));
        input.add(titleField);
        input.add(new JLabel("Description:"));
        input.add(descField);
        input.add(new JLabel("Urgency (0-10):"));
        urgencyField.setText("5");
        input.add(urgencyField);
        input.add(new JLabel("Importance (0-10):"));
        importanceField.setText("5");
        input.add(importanceField);
        input.add(new JLabel("Value (0-10):"));
        valueField.setText("5");
        input.add(valueField);

        main.add(input, BorderLayout.NORTH);

        decisionJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(decisionJList);
        main.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add/Save");
        JButton clearBtn = new JButton("Clear Fields");
        JButton deleteBtn = new JButton("Delete");
        JButton editBtn = new JButton("Edit Selected");
        JButton sortBtn = new JButton("Sort by Priority");
        JButton summaryBtn = new JButton("Show Summary");

        addBtn.addActionListener(new AddAction());
        clearBtn.addActionListener(new ClearAction());
        deleteBtn.addActionListener(new DeleteAction());
        editBtn.addActionListener(new EditAction());
        sortBtn.addActionListener(e -> {
            manager.sortDecisionsByPriorityDescending();
            refreshListFromManager();
        });
        summaryBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, manager.generateSummary(), "Summary", JOptionPane.INFORMATION_MESSAGE));

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        buttons.add(sortBtn);
        buttons.add(summaryBtn);
        buttons.add(clearBtn);

        main.add(buttons, BorderLayout.SOUTH);

        add(main);
    }

    private void refreshListFromManager() {
        listModel.clear();
        for (Decision d : manager.getDecisions()) listModel.addElement(d);
    }

    private Map<String, Integer> readFactorInputs() {
        Map<String, Integer> m = new HashMap<>();
        try {
            m.put("urgency", Integer.parseInt(urgencyField.getText()));
            m.put("importance", Integer.parseInt(importanceField.getText()));
            m.put("value", Integer.parseInt(valueField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter numeric values for factor fields.", "Input error", JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    private class AddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String t = titleField.getText().trim();
            String desc = descField.getText().trim();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(DecisionApp.this, "Title is required."); return; }
            Decision d = new Decision(t, desc);
            Map<String, Integer> factors = readFactorInputs();
            manager.addDecision(d, factors);
            refreshListFromManager();
            clearFields();
        }
    }

    private class ClearAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { clearFields(); }
    }

    private void clearFields() {
        titleField.setText("");
        descField.setText("");
        urgencyField.setText("5");
        importanceField.setText("5");
        valueField.setText("5");
    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Decision sel = decisionJList.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(DecisionApp.this, "Select a decision to delete."); return; }
            manager.removeDecision(sel);
            refreshListFromManager();
        }
    }

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Decision sel = decisionJList.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(DecisionApp.this, "Select a decision to edit."); return; }
            titleField.setText(sel.getTitle());
            descField.setText(sel.getDescription());
            Map<String, Integer> f = manager.getFactors(sel);
            urgencyField.setText(String.valueOf(f.getOrDefault("urgency", 5)));
            importanceField.setText(String.valueOf(f.getOrDefault("importance", 5)));
            valueField.setText(String.valueOf(f.getOrDefault("value", 5)));

            manager.removeDecision(sel);
            refreshListFromManager();
        }
    }
}
