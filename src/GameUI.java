import java.awt.*; // Ngimpor semua kelas buat grafis dasar dan UI (kayak Color, Font, LayoutManager)
import java.net.URL; // Ngimpor kelas buat ngurusin alamat URL (buat gambar)
import java.util.HashMap; // Ngimpor kelas HashMap buat nyimpen data key-value (dipake di save/load)
import java.util.List; // Ngimpor kelas List buat nampung kumpulan data (kayak item di inventory)
import javax.swing.*; // Ngimpor semua kelas Swing buat bikin UI modern (kayak JFrame, JButton, JTextArea)
import javax.swing.border.EmptyBorder; // Ngimpor kelas buat ngasih jarak kosong di pinggir komponen

public class GameUI extends JFrame { // Bikin kelas GameUI, ini adalah jendela utama game, turunan dari JFrame
    JTextArea textArea; // Deklarasi area buat nampilin teks cerita utama
    private JButton inventoryBtn, saveBtn; // Deklarasi tombol-tombol di bawah (inventory, save, balik ke menu)
    private Storyline currentStory; // Nyimpen objek cerita yang lagi jalan sekarang
    private GameState gameState; // Nyimpen semua data game (darah player, item, dll)
    private Typewriter typewriter; // Objek buat nampilin teks kayak mesin ketik di textArea utama
    private Typewriter battleLogTypewriter; // Objek buat nampilin teks kayak mesin ketik di log pertempuran

    private JPanel mainContentPanel; // Panel utama yang bisa ganti-ganti tampilan (antara cerita atau battle)
    private CardLayout cardLayout; // Layout buat ngatur ganti-ganti tampilan di mainContentPanel
    private static final String TEXT_AREA_CARD = "TextAreaCard"; // Nama buat kartu/tampilan cerita
    private static final String BATTLE_CARD = "BattleCard"; // Nama buat kartu/tampilan pertempuran

    private JPanel battleDisplayPanel; // Panel khusus buat nampilin semua info pas battle
    private JTextArea battleLogTextArea; // Area teks buat nampilin log/kejadian pas battle
    private JLabel playerHealthBattleLabel; // Label buat nampilin darah player pas battle
    private JLabel opponentNameBattleLabel; // Label buat nampilin nama musuh pas battle
    private JLabel opponentHealthBattleLabel; // Label buat nampilin darah musuh pas battle
    private JPanel battleActionPanel; // Panel buat nampung tombol aksi pas battle (serang, item)
    private JButton attackButton; // Tombol buat nyerang pas battle

    private JPanel topImagePanel; // Panel di bagian atas buat nampilin gambar suasana
    private Image currentStageImage; // Nyimpen gambar yang lagi ditampilin di topImagePanel

    public GameUI() { // Konstruktor, kode ini dijalanin pas objek GameUI pertama kali dibuat
        setTitle("Text Adventure"); // Ngasih judul buat jendela game
        setSize(800, 600); // Ngatur ukuran jendela game (lebar 800, tinggi 600)
        setLocationRelativeTo(null); // Naro jendela game di tengah layar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Kalo jendela ditutup, programnya berhenti
        setResizable(false); // Jendela game gak bisa diubah-ubah ukurannya

        JPanel mainFramePanel = new JPanel(new BorderLayout()); // Bikin panel utama buat nampung semua bagian UI, pake BorderLayout
        setContentPane(mainFramePanel); // Jadiin mainFramePanel sebagai panel utama di jendela ini

        // Modifikasi inisialisasi topImagePanel biar bisa ngegambar sendiri
        topImagePanel = new JPanel() { // Bikin panel buat gambar di atas, dengan cara ngegambar sendiri
            @Override // Ini nandain kalo kita mau ngubah cara kerja metode paintComponent dari kelas induknya
            protected void paintComponent(Graphics g) { // Metode ini dipanggil setiap kali panel perlu digambar ulang
                super.paintComponent(g); // Panggil dulu cara ngegambar standar dari kelas induknya
                if (currentStageImage != null) { // Kalo ada gambar yang mau ditampilin
                    // Skalain gambar biar pas sama ukuran panel
                    g.drawImage(currentStageImage, 0, 0, getWidth(), getHeight(), this); // Gambar currentStageImage di panel, dari pojok kiri atas (0,0), selebar dan setinggi panel
                } else { // Kalo gak ada gambar
                    // Kalo gak ada gambar, pake warna background yang udah diatur di bawah
                    // Atau bisa juga gambar tulisan/bentuk placeholder kalo mau
                }
            }
        };
        topImagePanel.setBackground(new Color(50, 50, 60)); // Warna background cadangan kalo gak ada gambar (abu-abu gelap)
        topImagePanel.setPreferredSize(new Dimension(800, 300)); // Ngatur ukuran preferensi panel gambar (lebar 800, tinggi 300)
        mainFramePanel.add(topImagePanel, BorderLayout.CENTER); // Tambahin panel gambar ke bagian tengah mainFramePanel

        JPanel bottomSectionPanel = new JPanel(new BorderLayout()); // Bikin panel buat bagian bawah (nampung area teks & tombol)
        bottomSectionPanel.setPreferredSize(new Dimension(800, 250)); // Ngatur ukuran preferensi panel bawah
        mainFramePanel.add(bottomSectionPanel, BorderLayout.SOUTH); // Tambahin panel bawah ke bagian bawah mainFramePanel
        
        textArea = new JTextArea(); // Bikin objek area teks utama
        textArea.setEditable(false); // Area teks gak bisa diedit sama user
        textArea.setLineWrap(true); // Kalo teksnya panjang, otomatis pindah baris
        textArea.setWrapStyleWord(true); // Pindah barisnya per kata, bukan per huruf
        textArea.setMargin(new Insets(10, 15, 10, 15)); // Ngasih jarak antara teks dan pinggir area teks
        textArea.setBackground(new Color(230, 230, 230)); // Ngatur warna background area teks (abu-abu terang)
        textArea.setForeground(Color.BLACK); // Ngatur warna teks jadi hitam
        this.typewriter = new Typewriter(textArea); // Bikin objek typewriter buat area teks ini
        JScrollPane textAreaScrollPane = new JScrollPane(textArea); // Bikin scrollbar buat area teks, kalo teksnya panjang
        textAreaScrollPane.setBorder(BorderFactory.createCompoundBorder( // Ngasih border gabungan (luar dan dalem)
            new EmptyBorder(5, 5, 5, 5), // Border luar: jarak kosong 5 pixel di semua sisi
            BorderFactory.createLineBorder(Color.DARK_GRAY) // Border dalem: garis warna abu-abu gelap
        ));

        cardLayout = new CardLayout(); // Bikin objek CardLayout buat ganti-ganti panel
        mainContentPanel = new JPanel(cardLayout); // Bikin panel yang pake CardLayout itu
        
        battleDisplayPanel = new JPanel(new BorderLayout(5, 5)); // Bikin panel buat tampilan battle, pake BorderLayout dengan jarak 5 pixel
        battleDisplayPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Ngasih jarak kosong 5 pixel di pinggir panel battle
        battleDisplayPanel.setBackground(new Color(220, 220, 220)); // Ngatur warna background panel battle (abu-abu agak terang)

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Bikin panel buat stats di battle, pake GridLayout (1 baris, 3 kolom, jarak horizontal 10, vertikal 0)
        statsPanel.setOpaque(false); // Bikin panel stats transparan (biar keliatan background battleDisplayPanel)
        playerHealthBattleLabel = new JLabel("Player HP: --", SwingConstants.LEFT); // Bikin label buat HP player, teks rata kiri
        opponentNameBattleLabel = new JLabel("Opponent: --", SwingConstants.CENTER); // Bikin label buat nama musuh, teks rata tengah
        opponentHealthBattleLabel = new JLabel("HP: --", SwingConstants.RIGHT); // Bikin label buat HP musuh, teks rata kanan
        statsPanel.add(playerHealthBattleLabel); // Tambahin label HP player ke panel stats
        statsPanel.add(opponentNameBattleLabel); // Tambahin label nama musuh ke panel stats
        statsPanel.add(opponentHealthBattleLabel); // Tambahin label HP musuh ke panel stats
        battleDisplayPanel.add(statsPanel, BorderLayout.NORTH); // Tambahin panel stats ke bagian atas panel battle

        battleLogTextArea = new JTextArea(); // Bikin area teks buat log battle
        battleLogTextArea.setEditable(false); // Log battle gak bisa diedit
        battleLogTextArea.setLineWrap(true); // Kalo lognya panjang, otomatis pindah baris
        battleLogTextArea.setWrapStyleWord(true); // Pindah barisnya per kata
        battleLogTextArea.setMargin(new Insets(10, 15, 10, 15)); // Ngasih jarak antara teks dan pinggir log battle
        battleLogTextArea.setBackground(new Color(240, 240, 240)); // Warna background log battle (abu-abu sangat terang)
        battleLogTextArea.setForeground(Color.BLACK); // Warna teks log battle jadi hitam
        this.battleLogTypewriter = new Typewriter(battleLogTextArea); // Bikin objek typewriter buat log battle ini
        JScrollPane battleLogScrollPane = new JScrollPane(battleLogTextArea); // Bikin scrollbar buat log battle
        battleLogScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Ngasih border garis abu-abu di log battle
        battleDisplayPanel.add(battleLogScrollPane, BorderLayout.CENTER); // Tambahin log battle (dengan scrollbar) ke tengah panel battle

        battleActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Bikin panel buat tombol aksi battle, pake FlowLayout (rata tengah, jarak horizontal 10, vertikal 5)
        battleActionPanel.setOpaque(false); // Bikin panel aksi battle transparan
        attackButton = new JButton("Attack"); // Bikin tombol "Attack"

        attackButton.addActionListener(e -> { // Ngasih aksi kalo tombol "Attack" diklik
            if (currentStory != null && battleManagerIsActive()) { // Kalo ada cerita yang jalan dan lagi battle
                currentStory.handleChoice(1); // Panggil metode handleChoice di cerita, dengan pilihan 1 (biasanya buat nyerang)
            }
        });

        battleActionPanel.add(attackButton); // Tambahin tombol "Attack" ke panel aksi battle
        battleDisplayPanel.add(battleActionPanel, BorderLayout.SOUTH); // Tambahin panel aksi battle ke bagian bawah panel battle

        mainContentPanel.add(textAreaScrollPane, TEXT_AREA_CARD); // Tambahin area teks cerita (dengan scrollbar) sebagai satu kartu/tampilan ke mainContentPanel
        mainContentPanel.add(battleDisplayPanel, BATTLE_CARD); // Tambahin panel battle sebagai kartu/tampilan lain ke mainContentPanel

        bottomSectionPanel.add(mainContentPanel, BorderLayout.CENTER); // Tambahin mainContentPanel (yang bisa ganti tampilan) ke tengah panel bawah
        cardLayout.show(mainContentPanel, TEXT_AREA_CARD); // Tampilan awal yang diliatin di mainContentPanel adalah area teks cerita

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Bikin panel buat tombol-tombol di paling bawah, FlowLayout (rata tengah, jarak horizontal & vertikal 10)
        bottomButtonPanel.setBorder(new EmptyBorder(5, 0, 10, 0)); // Ngasih jarak kosong di atas 5, bawah 10 (biar gak mepet)
        Font buttonFont = new Font("Arial", Font.BOLD, 14); // Ngatur jenis font, gaya (tebal), dan ukuran buat tombol
        Dimension buttonSize = new Dimension(130, 35); // Ngatur ukuran preferensi buat tombol (lebar 130, tinggi 35)

        inventoryBtn = new JButton("Inventory"); // Bikin tombol "Inventory"
        styleButton(inventoryBtn, buttonFont, buttonSize, new Color(70, 130, 180)); // Panggil metode styleButton buat ngasih gaya ke tombol inventory (biru)
        
        saveBtn = new JButton("Save Game"); // Bikin tombol "Save Game"
        styleButton(saveBtn, buttonFont, buttonSize, new Color(60, 179, 113)); // Panggil metode styleButton buat ngasih gaya ke tombol save (hijau)
        saveBtn.addActionListener(e -> saveCurrentGame()); // Ngasih aksi kalo tombol "Save Game" diklik, panggil metode saveCurrentGame

        inventoryBtn.addActionListener(e -> showInventory()); // Ngasih aksi kalo tombol "Inventory" diklik, panggil metode showInventory

        bottomButtonPanel.add(inventoryBtn); // Tambahin tombol inventory ke panel tombol bawah
        bottomButtonPanel.add(saveBtn); // Tambahin tombol save ke panel tombol bawah
                
        bottomSectionPanel.add(bottomButtonPanel, BorderLayout.SOUTH); // Tambahin panel tombol bawah ke bagian paling bawah dari bottomSectionPanel
    }

    private boolean battleManagerIsActive() { // Metode buat ngecek apakah lagi battle atau nggak
        if (currentStory instanceof Storyline1) { // Kalo cerita yang lagi jalan itu Storyline1
            return ((Storyline1) currentStory).getBattleManager().isBattleActive(); // Cek status battle dari BattleManager-nya Storyline1
        } else if (currentStory instanceof Storyline2) { // Kalo cerita yang lagi jalan itu Storyline2
            // Asumsi Storyline2 mungkin punya BattleManager nanti atau cara cek yang mirip
            // Kalo Storyline2 pasti gak ada battle, ini bisa disederhanain atau dihapus.
            BattleManager bm = ((Storyline2) currentStory).getBattleManager(); // Ambil BattleManager dari Storyline2
            return bm != null && bm.isBattleActive(); // Cek kalo BattleManager-nya ada dan lagi aktif
        } else if (currentStory instanceof Storyline3) { // Kalo cerita yang lagi jalan itu Storyline3
            return ((Storyline3) currentStory).getBattleManager().isBattleActive(); // Cek status battle dari BattleManager-nya Storyline3
        }
        return false; // Kalo gak ada cerita yang cocok atau gak ada BattleManager, anggap gak lagi battle
    }

    // Metode buat ngatur gambar di panel atas
    public void setStageImage(String imagePath) { // Parameter imagePath itu alamat file gambarnya
        if (imagePath == null || imagePath.isEmpty()) { // Kalo alamat gambarnya kosong atau null
            this.currentStageImage = null; // Gak ada gambar yang ditampilin
        } else { // Kalo ada alamat gambarnya
            try { // Coba lakuin ini, jaga-jaga kalo ada error pas ngeload gambar
                URL imgUrl = getClass().getResource(imagePath); // Dapetin URL gambar dari resource project
                if (imgUrl != null) { // Kalo URL gambarnya ketemu
                    this.currentStageImage = new ImageIcon(imgUrl).getImage(); // Load gambar dari URL itu jadi objek Image
                } else { // Kalo URL gambarnya gak ketemu
                    System.err.println("Image not found: " + imagePath); // Cetak pesan error ke konsol
                    this.currentStageImage = null; // Gak ada gambar yang ditampilin
                }
            } catch (Exception e) { // Kalo ada error lain pas ngeload gambar
                System.err.println("Error loading image: " + imagePath); // Cetak pesan error
                e.printStackTrace(); // Cetak detail errornya
                this.currentStageImage = null; // Gak ada gambar yang ditampilin
            }
        }
        topImagePanel.repaint(); // Perintahkan panel gambar buat ngegambar ulang dirinya (biar gambar baru muncul)
    }
    
    public void startGame(int storylineId) { // Metode buat mulai game baru dengan storyline tertentu
        this.gameState = new GameState(); // Bikin objek GameState baru (data game direset)
        setStageImage(null); // Hapus gambar suasana yang mungkin ada dari game sebelumnya
        switch (storylineId) { // Pilih storyline berdasarkan ID yang dikasih
            case 1 -> currentStory = new Storyline1(this, gameState); // Kalo ID 1, mulai Storyline1
            case 2 -> currentStory = new Storyline2(this, gameState); // Kalo ID 2, mulai Storyline2
            case 3 -> currentStory = new Storyline3(this, gameState); // Kalo ID 3, mulai Storyline3
            default -> { // Kalo ID-nya gak dikenal
                displayText("Error: Invalid storyline ID.", Color.RED); // Tampilin pesan error
                return; // Keluar dari metode ini
            }
        }
        if (currentStory != null) { // Kalo objek ceritanya berhasil dibuat
            currentStory.startStory(); // Mulai ceritanya
        }
    }

    public void showBattleInterface(String opponentName, int playerHealth, int opponentHealth) { // Metode buat nampilin UI battle
        playerHealthBattleLabel.setText("Player HP: " + playerHealth); // Update label HP player
        opponentNameBattleLabel.setText(opponentName); // Update label nama musuh
        opponentHealthBattleLabel.setText("HP: " + opponentHealth); // Update label HP musuh
        battleLogTextArea.setText(""); // Kosongin log battle sebelumnya
        if (battleLogTypewriter != null) { // Kalo ada objek typewriter buat log battle
            battleLogTypewriter.stopAndClearQueue(); // Berhentiin dan kosongin antrian teks typewriter log battle
        }
        cardLayout.show(mainContentPanel, BATTLE_CARD); // Ganti tampilan di mainContentPanel jadi tampilan battle
    }

    public void updateBattleInterfaceHealth(int playerHealth, int opponentHealth) { // Metode buat update HP di UI battle
        playerHealthBattleLabel.setText("Player HP: " + playerHealth); // Update label HP player
        opponentHealthBattleLabel.setText("HP: " + opponentHealth); // Update label HP musuh
    }

    public void appendBattleLog(String message, Color color) { // Metode buat nambahin teks ke log battle
        int shortDelay = 15; // Atur delay ketikan yang cepet buat log battle
        this.battleLogTypewriter.typeText(message + "\n", color != null ? color : Color.BLACK, shortDelay); // Pake typewriter buat nampilin pesan di log battle, tambah baris baru, pake warna yang dikasih (atau hitam kalo null), dengan delay cepet
    }

    public void hideBattleInterface() { // Metode buat nyembunyiin UI battle
        cardLayout.show(mainContentPanel, TEXT_AREA_CARD); // Ganti tampilan di mainContentPanel balik ke tampilan cerita
    }


    public void showChoicesDialog(String[] options) { // Metode buat nampilin dialog pilihan buat player
        if (options == null || options.length == 0) return; // Kalo gak ada pilihan, gak usah ngapa-ngapain

        JDialog dialogueDialog = new JDialog(this, "Dialogue Choices", true); // Bikin dialog baru, modal (ngeblok jendela utama)
        dialogueDialog.setLayout(new BoxLayout(dialogueDialog.getContentPane(), BoxLayout.Y_AXIS)); // Atur layout dialog biar komponennya numpuk vertikal
        dialogueDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Kalo dialog ditutup (misal pake tombol X), jangan lakuin apa-apa (biar gak error)

        DefaultListModel<String> listModel = new DefaultListModel<>(); // Model buat nampung daftar pilihan
        for (String option : options) { // Loop semua pilihan yang dikasih
            listModel.addElement(option); // Tambahin tiap pilihan ke model daftar
        }
        JList<String> optionList = new JList<>(listModel); // Bikin JList (daftar yang bisa dipilih) dari model tadi
        JButton btn = new JButton("Select"); // Bikin tombol "Select"
        btn.addActionListener(e -> { // Ngasih aksi kalo tombol "Select" diklik
            int selectedIdx = optionList.getSelectedIndex(); // Dapetin nomor urut pilihan yang dipilih player
            dialogueDialog.dispose(); // Tutup dialog pilihan
            if (selectedIdx >= 0 && currentStory != null) { // Kalo ada pilihan yang valid dan ada cerita yang jalan
                currentStory.handleChoice(selectedIdx + 1); // Panggil handleChoice di cerita, pake nomor pilihan (karena JList mulai dari 0, pilihan game biasanya mulai dari 1)
            }
        });
        dialogueDialog.add(new JScrollPane(optionList)); // Tambahin daftar pilihan (dengan scrollbar) ke dialog
        dialogueDialog.add(btn, BorderLayout.SOUTH); // Tambahin tombol "Select" ke bagian bawah dialog
        dialogueDialog.pack(); // Atur ukuran dialog biar pas sama isinya

        int fixedWidth = 400; // Lebar dialog yang diinginkan
        int maxHeight = 250; // Tinggi maksimal dialog
        int preferredHeightWithPadding = dialogueDialog.getPreferredSize().height + 50; // Tinggi preferensi dialog ditambah sedikit padding
        dialogueDialog.setSize(fixedWidth, Math.min(maxHeight, preferredHeightWithPadding)); // Atur ukuran dialog: lebar tetap, tinggi sesuai preferensi tapi gak lebih dari maxHeight
        
        dialogueDialog.setLocationRelativeTo(null); // Naro dialog di tengah layar
        dialogueDialog.setResizable(false); // Dialog gak bisa diubah ukurannya
        dialogueDialog.setVisible(true); // Tampilin dialognya
    }

    public void displayText(String text, Color color) { // Metode buat nampilin teks di area cerita utama
        this.typewriter.typeText(text, color != null ? color : Color.BLACK, 20); // Pake typewriter buat nampilin teks, pake warna yang dikasih (atau hitam kalo null), dengan delay ketikan 20ms
    }

    private void showInventory() { // Metode buat nampilin inventory player
        if (gameState == null) { // Kalo GameState belum ada
            displayText("\nCannot open inventory: GameState not initialized.", Color.RED); // Tampilin pesan error
            return; // Keluar
        }

        JDialog inventoryDialog = new JDialog(this, "Inventory", true); // Bikin dialog inventory, modal
        inventoryDialog.setSize(450, 300); // Atur ukuran dialog inventory
        inventoryDialog.setLayout(new BorderLayout()); // Pake BorderLayout buat dialog inventory

        List<String> itemsForDisplay = gameState.getInventoryForDisplay(); // Dapetin daftar item buat ditampilin dari GameState
        DefaultListModel<String> listModel = new DefaultListModel<>(); // Model buat daftar item di inventory
        for (String itemDisplayString : itemsForDisplay) { // Loop semua item yang mau ditampilin
            listModel.addElement(itemDisplayString); // Tambahin tiap item ke model daftar
        }

        JList<String> itemList = new JList<>(listModel); // Bikin JList dari model item
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Cuma bisa milih satu item sekali waktu
        
        JButton useBtn = new JButton("Use Selected"); // Bikin tombol "Use Selected"
        useBtn.setEnabled(!itemsForDisplay.isEmpty() && !itemsForDisplay.get(0).equals("Your inventory is empty.")); // Tombol "Use" aktif kalo inventory gak kosong

        useBtn.addActionListener(e -> { // Ngasih aksi kalo tombol "Use Selected" diklik
            String selectedValue = itemList.getSelectedValue(); // Dapetin item yang dipilih dari daftar
            if (selectedValue != null && !selectedValue.equals("Your inventory is empty.")) { // Kalo ada item yang dipilih dan bukan pesan "inventory kosong"
                // Ambil nama item (misal "Cigarette" dari "Cigarette (x2) (A standard...)")
                String itemName = selectedValue.substring(0, selectedValue.indexOf(" (x")); // Potong string buat dapetin nama itemnya aja
                inventoryDialog.dispose(); // Tutup dialog inventory

                // Handle battle-specific item usage
                if (battleManagerIsActive()) {
                    handleBattleItemUsage(itemName);
                } else {
                    handleNormalItemUsage(itemName);
                }
            } else { // Kalo gak ada item yang dipilih atau inventory kosong
                 displayText("\nNo item selected or inventory is empty.", Color.BLACK); // Tampilin pesan
            }
        });

        inventoryDialog.add(new JScrollPane(itemList), BorderLayout.CENTER); // Tambahin daftar item (dengan scrollbar) ke tengah dialog inventory
        inventoryDialog.add(useBtn, BorderLayout.SOUTH); // Tambahin tombol "Use Selected" ke bawah dialog inventory
        inventoryDialog.setLocationRelativeTo(this); // Naro dialog inventory di tengah layar
        inventoryDialog.setVisible(true); // Tampilin dialog inventory
    }

    // Add new method for battle item usage
    private void handleBattleItemUsage(String itemName) {
        if (currentStory instanceof Storyline1) {
            // For Storyline1, you might want to add useInventoryItem method
            Item item = gameState.getItemPrototype(itemName);
            if (item != null && gameState.getItemQuantity(itemName) > 0) {
                item.applyEffect(gameState, this, "Player");
                gameState.consumeItem(itemName);
                appendBattleLog("Used " + itemName + " during battle!", Color.GREEN);
            } else {
                appendBattleLog("Cannot use " + itemName + ".", Color.RED);
            }
        } else if (currentStory instanceof Storyline2) {
            ((Storyline2) currentStory).useInventoryItem(itemName);
        } else if (currentStory instanceof Storyline3) {
            ((Storyline3) currentStory).useInventoryItem(itemName);
        }
    }

    // Add new method for normal (non-battle) item usage
    private void handleNormalItemUsage(String itemName) {
        if (currentStory instanceof Storyline3) {
            ((Storyline3) currentStory).useInventoryItem(itemName);
        } else if (currentStory instanceof Storyline2) {
            ((Storyline2) currentStory).useInventoryItem(itemName);
        } else {
            // Generic item usage for other storylines
            Item item = gameState.getItemPrototype(itemName);
            if (item != null && gameState.getItemQuantity(itemName) > 0) {
                item.applyEffect(gameState, this, "Player");
                gameState.consumeItem(itemName);
                displayText("\nUsed: " + itemName + ".", Color.BLACK);
            } else {
                displayText("\nCannot use " + itemName + ".", Color.ORANGE);
            }
        }
    }

    private void saveCurrentGame() { // Metode buat nyimpen game
        if (currentStory == null || gameState == null) { // Kalo gak ada cerita atau data game
            displayText("\nNothing to save.", Color.BLACK); // Tampilin pesan
            return; // Keluar
        }
        if (battleManagerIsActive()) { // Kalo lagi battle
            displayText("\nCannot save during an active battle.", Color.RED); // Tampilin pesan error
            return; // Keluar
        }

        int storylineId = -1; // Variabel buat nyimpen ID storyline
        int currentDialogueState = -1; // Variabel buat nyimpen state dialog terakhir

        if (currentStory instanceof Storyline1) { // Kalo cerita yang jalan Storyline1
            storylineId = 1; // ID-nya 1
            currentDialogueState = ((Storyline1) currentStory).getDialogueState(); // Ambil state dialog dari Storyline1
        } else if (currentStory instanceof Storyline2) { // Kalo cerita yang jalan Storyline2
            storylineId = 2; // ID-nya 2
            currentDialogueState = ((Storyline2) currentStory).getDialogueState(); // Ambil state dialog dari Storyline2
        }  else if (currentStory instanceof Storyline3) { // Kalo cerita yang jalan Storyline3
            storylineId = 3; // ID-nya 3
            currentDialogueState = ((Storyline3) currentStory).getDialogueState(); // Ambil state dialog dari Storyline3
        } else { // Kalo tipe ceritanya gak dikenal
            displayText("\nCannot determine storyline type to save.", Color.RED); // Tampilin pesan error
            return; // Keluar
        }

        // Minta user milih slot buat nyimpen (1-3)
        String[] options = { // Pilihan slot yang ditampilin ke user
            "Slot 1: " + SaveManager.getSlotStage(1), // Slot 1 dengan deskripsi storyline dan stage-nya
            "Slot 2: " + SaveManager.getSlotStage(2), // Slot 2 dengan deskripsi storyline dan stage-nya
            "Slot 3: " + SaveManager.getSlotStage(3)  // Slot 3 dengan deskripsi storyline dan stage-nya
        };
        String choice = (String) JOptionPane.showInputDialog( // Tampilin dialog input buat milih slot
            this, // Jendela induknya GameUI ini
            "Select a slot to save:", // Pesan di dialog
            "Save Game", // Judul dialog
            JOptionPane.PLAIN_MESSAGE, // Tipe pesan dialog
            null, // Icon (gak pake)
            options, // Pilihan yang bisa dipilih
            options[0] // Pilihan default
        );
        if (choice == null) { // Kalo user batal milih (klik cancel atau X)
            displayText("\nSave cancelled.", Color.BLACK); // Tampilin pesan batal
            return; // Keluar
        }
        int slot = choice.startsWith("Slot 1") ? 1 : choice.startsWith("Slot 2") ? 2 : 3; // Tentukan nomor slot berdasarkan pilihan user

        SaveData saveData = new SaveData( // Bikin objek SaveData buat nyimpen semua data game
            storylineId, // ID storyline
            currentDialogueState, // State dialog terakhir
            gameState.getAllStats(), // Semua stats player
            gameState.getAllFlags(), // Semua flag/penanda di game
            new HashMap<>(gameState.getInventoryQuantities()) // Salinan kuantitas item di inventory
        );

        try { // Coba simpen data
            SaveManager.saveGame(saveData, slot); // Panggil metode saveGame dari SaveManager
            displayText("\nGame Saved to slot " + slot + "!", Color.BLACK); // Tampilin pesan berhasil
        } catch (Exception ex) { // Kalo gagal nyimpen
            displayText("\nFailed to save game: " + ex.getMessage(), Color.RED); // Tampilin pesan error
            ex.printStackTrace(); // Cetak detail errornya
        }
    }

    public void applySaveData(SaveData data) { // Metode buat nerapin data save-an ke game
        if (data == null) { // Kalo data save-annya null (gak valid)
            displayText("Failed to load save data.", Color.RED); // Tampilin pesan error
            return; // Keluar
        }

        setStageImage(null); // Hapus gambar suasana yang mungkin ada

        this.gameState = new GameState(); // Bikin objek GameState baru
        this.gameState.setAllStats(data.stats); // Atur semua stats player dari data save-an
        this.gameState.setAllFlags(data.flags); // Atur semua flag dari data save-an
        this.gameState.setInventoryQuantities(data.inventoryQuantities); // Atur inventory dari data save-an

        textArea.setText(""); // Kosongin area teks utama
        if (this.typewriter != null) { // Kalo ada typewriter
            this.typewriter.stopAndClearQueue(); // Berhentiin dan kosongin antrian teks typewriter
        }

        switch (data.storylineId) { // Pilih storyline berdasarkan ID dari data save-an
            case 1: // Kalo ID 1
                currentStory = new Storyline1(this, gameState); // Bikin objek Storyline1 baru
                ((Storyline1) currentStory).setDialogueState(data.dialogueState); // Atur state dialognya
                ((Storyline1) currentStory).showDialoguePublic(data.dialogueState); // Lanjutin cerita dari state itu
                break;
            case 2: // Kalo ID 2
                currentStory = new Storyline2(this, gameState); // Bikin objek Storyline2 baru
                ((Storyline2) currentStory).setDialogueState(data.dialogueState); // Atur state dialognya
                ((Storyline2) currentStory).startStory(true); // pass true for fromSave
                break;
            case 3: // Kalo ID 3
                currentStory = new Storyline3(this, gameState); // Bikin objek Storyline3 baru
                ((Storyline3) currentStory).setDialogueState(data.dialogueState); // Atur state dialognya
                ((Storyline3) currentStory).showDialoguePublic(data.dialogueState); // Lanjutin cerita dari state itu
                break;
            default: // Kalo tipe ceritanya gak dikenal
                displayText("Error: Invalid storyline ID in save data: " + data.storylineId, Color.RED); // Tampilin pesan error
                return; // Keluar
        }

        displayText("\nGame Loaded!", Color.BLACK); // Tampilin pesan game berhasil diload
    }
    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) { // Metode buat ngasih gaya ke tombol
        button.setFont(font); // Atur font tombol
        button.setPreferredSize(size); // Atur ukuran preferensi tombol
        button.setBackground(bgColor); // Atur warna background tombol
        button.setForeground(Color.WHITE); // Atur warna teks tombol jadi putih
        button.setFocusPainted(false); // Hilangin border aneh pas tombol difokusin
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // Ngasih border kayak tombol 3D yang nongol
        Color hoverColor = bgColor.brighter(); // Warna pas mouse di atas tombol (lebih terang)
        Color pressedColor = bgColor.darker(); // Warna pas tombol ditekan (lebih gelap)

        button.addMouseListener(new java.awt.event.MouseAdapter() { // Ngasih listener buat aksi mouse di tombol
            public void mouseEntered(java.awt.event.MouseEvent evt) { // Pas mouse masuk area tombol
                button.setBackground(hoverColor); // Ganti warna background jadi hoverColor
            }
            public void mouseExited(java.awt.event.MouseEvent evt) { // Pas mouse keluar area tombol
                button.setBackground(bgColor); // Balikin warna background ke warna asli
            }
            public void mousePressed(java.awt.event.MouseEvent evt) { // Pas tombol ditekan
                button.setBackground(pressedColor); // Ganti warna background jadi pressedColor
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) { // Pas tombol dilepas setelah ditekan
                if (button.getBounds().contains(evt.getPoint())) { // Kalo mouse masih di dalem area tombol pas dilepas
                     button.setBackground(hoverColor); // Balikin ke warna hover
                } else { // Kalo mouse udah di luar area tombol pas dilepas
                     button.setBackground(bgColor); // Balikin ke warna asli
                }
            }
        });
    }


    // Update your load save method (wherever it is in GameUI)
    public void loadSaveGame(int slot) {
        SaveData data = SaveManager.loadGame(slot);
        if (data != null) {
            applySaveData(data);
        } else {
            displayText("\nNo save found in slot " + slot + ".", Color.RED);
        }
    }
}