public class ServidorBuilder {

    private Servidor servidor;

    public ServidorBuilder(Servidor servidorBase) {
        this.servidor = servidorBase;
    }

    public ServidorBuilder setSistemaOperativo(String so) {
        servidor.sistemaOperativo = so;
        return this;
    }

    public ServidorBuilder setCPU(int cpu) {
        servidor.cpu = cpu;
        return this;
    }

    public ServidorBuilder setRAM(int ram) {
        servidor.ram = ram;
        return this;
    }

    public ServidorBuilder setAlmacenamiento(int almacenamiento) {
        servidor.almacenamiento = almacenamiento;
        return this;
    }

    public ServidorBuilder setBaseDatos(String bd) {
        servidor.baseDatos = bd;
        return this;
    }

    public ServidorBuilder setFirewall(boolean firewall) {
        servidor.firewall = firewall;
        return this;
    }

    public ServidorBuilder setBackup(boolean backup) {
        servidor.backup = backup;
        return this;
    }

    public Servidor build() {
        return servidor;
    }
}