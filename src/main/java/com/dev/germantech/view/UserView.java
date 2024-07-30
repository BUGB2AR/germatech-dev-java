package com.dev.germantech.view;

import com.dev.germantech.controller.UserController;
import com.dev.germantech.model.User;
import com.dev.germantech.utils.CpfUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.List;
import javax.swing.text.MaskFormatter;

public class UserView extends JFrame {

    private UserController userController;
    private JTextField idField;
    private JTextField nameField;
    private JFormattedTextField cpfField;
    private JFormattedTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserView(UserController userController) {
        this.userController = userController;
        createView();
    }

    private void createView() {
        setTitle("TESTE DESENVOLVEDOR JAVA");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID (informe caso queira atualizar o usuario da listagem):"), gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1;
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfMask);
            cpfField.setColumns(15);
            formPanel.add(cpfField, gbc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1;
        try {
            MaskFormatter phoneMask = new MaskFormatter("(##) #####-####");
            phoneMask.setPlaceholderCharacter('_');
            phoneField = new JFormattedTextField(phoneMask);
            phoneField.setColumns(15);
            formPanel.add(phoneField, gbc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;

        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener(e -> addUser());
        formPanel.add(addButton, gbc);

        gbc.gridy++;
        JButton updateButton = new JButton("Atualizar");
        updateButton.addActionListener(e -> updateUser());
        formPanel.add(updateButton, gbc);

        gbc.gridy++;
        JButton listButton = new JButton("Listar");
        listButton.addActionListener(e -> listUsers());
        formPanel.add(listButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Email", "Telefone"}, 0);

        userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = userTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int userId = (int) tableModel.getValueAt(row, 0);
                    deleteUser(userId);
                }
            }
        });

        setVisible(true);
    }

    private void addUser() {
        try {
            if (isAnyFieldEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cpf = cpfField.getText().replace(".", "").replace("-", "");
            if (!CpfUtils.isValidCpf(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userController.findByCpf(cpf) != null) {
                JOptionPane.showMessageDialog(this, "Usuário com CPF já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userController.addUser(nameField.getText(),
                    cpf,
                    emailField.getText(),
                    phoneField.getText().replace("(", "").replace(") ", "").replace("-", "").replace("_", ""),
                    new String(passwordField.getPassword()));
            JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
            listUsers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        try {
            if (isAnyFieldEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String cpf = cpfField.getText().replace(".", "").replace("-", "");
            if (!CpfUtils.isValidCpf(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User existingUserById = userController.findById(id);
            if (existingUserById == null) {
                JOptionPane.showMessageDialog(this, "Usuário com ID informado não existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User existingUserByCpf = userController.findByCpf(cpf);
            if (existingUserByCpf != null && existingUserByCpf.getId() != id) {
                JOptionPane.showMessageDialog(this, "Usuário com CPF já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(id, nameField.getText(),
                    cpf,
                    emailField.getText(),
                    phoneField.getText().replace("(", "").replace(") ", "").replace("-", "").replace("_", ""),
                    new String(passwordField.getPassword()));

            userController.updateUser(user);
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            listUsers();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isAnyFieldEmpty() {
        return nameField.getText().trim().isEmpty() ||
                cpfField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                new String(passwordField.getPassword()).trim().isEmpty();
    }

    private void deleteUser(int id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Você realmente deseja excluir este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userController.deleteUserById(id);
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                listUsers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listUsers() {
        List<User> users = userController.listUsers();
        tableModel.setRowCount(0);
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName(), user.getCpf(), user.getEmail(), user.getPhone()});
        }
    }
}
