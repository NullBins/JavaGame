import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class AdventureGame {
    // 플레이어 상태 전역 변수
    static String playerName = "";
    static int hp = 100;
    static int gold = 0;
    static int potions = 1;

    // 게임 실행 상태
    static boolean gameRunning = true;

    // 저장 파일 이름
    static final String SAVE_FILE = "player_data.txt";

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       [ 미지의 던전 탐험기 ]       ");
        System.out.println("========================================");

        // 처음부터 시작하거나 이어서 시작
        selectGameStart();

        // HP가 0보다 크고 게임이 실행 중인 동안 반복
        while (hp > 0 && gameRunning) {
            showTown();
        }

        // HP가 0 이하라면 게임 오버
        if (hp <= 0) {
            System.out.println("\n[ 사유 ] 당신의 HP가 0이 되었습니다.");
            System.out.println("당신의 모험은 여기서 끝이 났습니다. (GAME OVER)");
        }
    }

    // 처음부터 시작 / 이어서 시작 선택
    static void selectGameStart() {
        while (true) {
            System.out.println("\n1. 처음부터 시작한다.");
            System.out.println("2. 이어서 시작한다.");
            System.out.print("▶ 당신의 선택 (1-2): ");

            int choice = inputNumber();

            if (choice == 1) {
                // 플레이어 이름 입력
                System.out.print("플레이어 이름을 입력해주세요: ");
                playerName = sc.nextLine();

                // 처음부터 시작하므로 데이터 초기화
                hp = 100;
                gold = 0;
                potions = 1;

                System.out.println("\n" + playerName + "님의 새로운 모험을 시작합니다.");

                // 초기 데이터 저장
                savePlayerData();
                return;

            } else if (choice == 2) {
                // 저장된 데이터 불러오기
                if (loadPlayerData()) {
                    System.out.println("\n" + playerName + "님의 저장 데이터를 불러왔습니다.");
                    return;
                } else {
                    System.out.println("\n저장된 플레이어 데이터가 없습니다.");
                    System.out.println("처음부터 시작해주세요.");
                }

            } else {
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    }

    // 숫자가 아닌 문자를 입력했을 때 예외 처리
    static int inputNumber() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.print("숫자만 입력해주세요. 다시 입력: ");
            }
        }
    }

    // 플레이어 데이터 저장
    static void savePlayerData() {
        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(SAVE_FILE))) {

            writer.println(playerName);
            writer.println(hp);
            writer.println(gold);
            writer.println(potions);

        } catch (IOException e) {
            System.out.println("플레이어 데이터 저장 중 오류가 발생했습니다.");
        }
    }

    // 플레이어 데이터 불러오기
    static boolean loadPlayerData() {
        File saveFile = new File(SAVE_FILE);

        // 저장 파일이 없으면 false 반환
        if (!saveFile.exists()) {
            return false;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(saveFile))) {

            playerName = reader.readLine();
            hp = Integer.parseInt(reader.readLine());
            gold = Integer.parseInt(reader.readLine());
            potions = Integer.parseInt(reader.readLine());

            return true;

        } catch (IOException | NumberFormatException e) {
            System.out.println("플레이어 데이터를 불러오는 중 오류가 발생했습니다.");
            return false;
        }
    }

    // 마을
    static void showTown() {
        System.out.println("\n----------------------------------------");
        System.out.println("[ 플레이어 ] " + playerName);
        System.out.println("[ 현재 상태 ] HP: " + hp
                + " | 골드: " + gold
                + " | 포션: " + potions + "개");
        System.out.println("----------------------------------------");
        System.out.println(" 당신은 평화로운 마을 광장에 있습니다.");
        System.out.println(" 어디로 이동하시겠습니까?");
        System.out.println("----------------------------------------");
        System.out.println("1. 어두운 던전으로 향한다.");
        System.out.println("2. 잡화점에서 포션을 산다. (비용: 30골드)");
        System.out.println("3. 여관에서 휴식한다. (비용: 20골드, HP: 50회복)");
        System.out.println("4. 플레이어 데이터를 저장하고 게임을 종료한다.");
        System.out.print("▶ 당신의 선택 (1-4): ");

        int choice = inputNumber();

        if (choice == 1) {
            enterDungeon();

        } else if (choice == 2) {
            buyPotion();

        } else if (choice == 3) {
            restAtInn();

        } else if (choice == 4) {
            // 현재 플레이어 데이터 저장
            savePlayerData();

            // 게임 반복문 종료
            gameRunning = false;

            System.out.println("\n플레이어 데이터를 저장했습니다.");
            System.out.println(playerName + "님의 게임을 종료합니다.");

        } else {
            System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
        }
    }

    // 던전 진입
    static void enterDungeon() {
        System.out.println("----------------------------------------");
        System.out.println("\n( ... 던전으로 진입합니다 ... )");
        System.out.println("안쪽에서 몬스터의 소리가 들립니다.");

        int event = rand.nextInt(10); // 0 ~ 9 사이 랜덤 숫자 생성

        if (event < 5) { // 50% 확률로 몬스터 조우
            encounterMonster();

        } else if (event < 8) { // 30% 확률로 보물 발견
            System.out.println("\n[ 이벤트 발생! ]");
            System.out.println("던전 구석에서 낡은 보물상자를 발견했습니다!");

            int findGold = 20 + rand.nextInt(31); // 20~50 골드 획득
            gold += findGold;

            System.out.println(findGold
                    + " 골드를 획득했습니다! (현재 골드: "
                    + gold + ")");

        } else { // 20% 확률로 함정
            System.out.println("\n[ 경고! ]");
            System.out.println("앗! 발을 헛디뎌 함정에 빠졌습니다.");

            hp -= 15;

            System.out.println("체력을 15 잃었습니다. (현재 HP: "
                    + hp + ")");
        }
    }

    // 몬스터 전투
    static void encounterMonster() {
        System.out.println("\n[ 전투 발생! ]");
        System.out.println("험악하게 생긴 [고블린]이 나타났습니다!");

        while (true) {
            System.out.println("\n1. 싸운다 (체력 소모 예상, 골드 획득)");
            System.out.println("2. 포션 마시기 (현재 " + potions + "개 보유)");
            System.out.println("3. 도망친다 (확률적 성공)");
            System.out.print("▶ 당신의 선택 (1-3): ");

            int action = inputNumber();

            if (action == 1) {
                int damage = 10 + rand.nextInt(21);
                int reward = 30;

                hp -= damage;
                gold += reward;

                System.out.println("\n치열한 전투 끝에 고블린을 물리쳤습니다!");
                System.out.println(damage
                        + "의 피해를 입었지만, "
                        + reward
                        + " 골드를 얻었습니다.");

                break;

            } else if (action == 2) {
                if (potions > 0) {
                    potions--;
                    hp += 40;

                    if (hp > 100) {
                        hp = 100;
                    }

                    System.out.println("\n포션을 마셔 체력을 40 회복했습니다!"
                            + " (현재 HP: " + hp + ")");

                } else {
                    System.out.println("\n포션이 없습니다!");
                }

            } else if (action == 3) {
                if (rand.nextBoolean()) {
                    System.out.println("\n무사히 마을로 도망쳤습니다.");
                    break;

                } else {
                    System.out.println("\n도망치는데 실패했습니다!"
                            + " 고블린에게 등짝을 맞아 체력을 10 잃었습니다.");

                    hp -= 10;
                }

            } else {
                System.out.println("잘못된 입력입니다.");
            }

            if (hp <= 0) {
                return;
            }
        }
    }

    // 잡화점
    static void buyPotion() {
        System.out.println("\n[ 잡화점 ]");

        if (gold >= 30) {
            gold -= 30;
            potions++;

            System.out.println("30골드를 지불하고 포션을 1개 구매했습니다.");

        } else {
            System.out.println("골드가 부족합니다!"
                    + " (필요 골드: 30, 현재 골드: "
                    + gold + ")");
        }
    }

    // 여관
    static void restAtInn() {
        System.out.println("\n[ 여관 ]");

        if (gold >= 20) {
            gold -= 20;
            hp += 50;

            if (hp > 100) {
                hp = 100;
            }

            System.out.println("푹 쉬고 일어났습니다."
                    + " 체력이 회복되었습니다. (현재 HP: "
                    + hp + ")");

        } else {
            System.out.println("골드가 부족합니다!");
        }
    }
}
