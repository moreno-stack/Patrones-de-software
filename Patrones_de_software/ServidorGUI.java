import javax.swing.*;
import java.awt.*;

public class ServidorGUI extends JFrame {

    JComboBox<String> plantillaBox, soBox, bdBox;
    JSpinner cpu, ram, almacenamiento;
    JCheckBox firewall, backup;
    JTextArea salida;

    public ServidorGUI() {
        setTitle("Configuración de Servidores en la Nube");
        setSize(500, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        plantillaBox = new JComboBox<>(new String[]{
                "Servidor Web", "Servidor Base de Datos", "Servidor Aplicaciones"
        });
        soBox = new JComboBox<>(new String[]{"Ubuntu", "Windows Server"});
        bdBox = new JComboBox<>(new String[]{"MySQL", "PostgreSQL", "MongoDB"});

        cpu = new JSpinner(new SpinnerNumberModel(4, 1, 32, 1));
        ram = new JSpinner(new SpinnerNumberModel(8, 2, 128, 2));
        almacenamiento = new JSpinner(new SpinnerNumberModel(200, 50, 2000, 50));

        firewall = new JCheckBox("Firewall");
        backup = new JCheckBox("Backup");

        panel.add(new JLabel("Plantilla Base:"));
        panel.add(plantillaBox);
        panel.add(new JLabel("Sistema Operativo:"));
        panel.add(soBox);
        panel.add(new JLabel("CPU:"));
        panel.add(cpu);
        panel.add(new JLabel("RAM (GB):"));
        panel.add(ram);
        panel.add(new JLabel("Almacenamiento (GB):"));
        panel.add(almacenamiento);
        panel.add(new JLabel("Base de Datos:"));
        panel.add(bdBox);
        panel.add(firewall);
        panel.add(backup);

        JButton crear = new JButton("Crear Servidor");
        salida = new JTextArea();
        salida.setEditable(false);

        crear.addActionListener(e -> crearServidor());

        add(panel, BorderLayout.NORTH);
        add(crear, BorderLayout.CENTER);
        add(new JScrollPane(salida), BorderLayout.SOUTH);
    }

    private void crearServidor() {
        // PROTOTYPE
        Servidor base = new Servidor(plantillaBox.getSelectedItem().toString());

        Servidor clon = (Servidor) base.clonar();

        // BUILDER
        Servidor servidorFinal = new ServidorBuilder(clon)
                .setSistemaOperativo(soBox.getSelectedItem().toString())
                .setCPU((int) cpu.getValue())
                .setRAM((int) ram.getValue())
                .setAlmacenamiento((int) almacenamiento.getValue())
                .setBaseDatos(bdBox.getSelectedItem().toString())
                .setFirewall(firewall.isSelected())
                .setBackup(backup.isSelected())
                .build();

        salida.setText(servidorFinal.mostrar());
    }
}