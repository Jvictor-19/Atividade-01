import java.util.Random;

public class TesteBancoDeDados {
     public static void main(String[] args) {
        BancoDeDados banco = new BancoDeDados();
        Random rand = new Random();

        for (int i = 1; i <= 30; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    int operacao = rand.nextInt(4);
                        switch (operacao) {
                            case 0:
                                banco.create(id);
                                break;
                            case 1:
                                banco.update(id);
                                break;
                            case 2:
                                banco.delete(id);
                                break;
                            case 3:
                                banco.readOnly(id);
                                break;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
