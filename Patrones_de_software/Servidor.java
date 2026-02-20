public class Servidor implements ServidorPrototype {

    String tipo;
    String sistemaOperativo;
    int cpu;
    int ram;
    int almacenamiento;
    String baseDatos;
    boolean firewall;
    boolean backup;

    public Servidor(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public ServidorPrototype clonar() {
        try {
            return (Servidor) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String mostrar() {
        return "Tipo: " + tipo +
                "\nSO: " + sistemaOperativo +
                "\nCPU: " + cpu +
                "\nRAM: " + ram + " GB" +
                "\nAlmacenamiento: " + almacenamiento + " GB" +
                "\nBase de datos: " + baseDatos +
                "\nFirewall: " + firewall +
                "\nBackup: " + backup;
    }
}