# ⬡ Space Colony — OOP Project

An Android application developed in Java using Android Studio for the Object-Oriented Programming course.

## 👥 Team Members
- Enkhjin Sanchir
- Oyungerel Munkhjin
- Darikhishig Batbaatar

## 📱 About
Space Colony is a crew management game set in space. The player recruits crew members, trains them in a simulator, and sends them on cooperative turn-based missions against system-generated threats.

## 🎮 How to Play
1. **Recruit** — Go to Recruit tab, pick a name and specialization
2. **Train** — Move crew to Simulator, train them to gain XP (+1 skill per XP)
3. **Mission** — Move 2–3 crew to Mission Control, launch a mission
4. **Fight** — Choose Attack, Defend or Special each turn
5. **Recover** — Survivors return to Quarters, fallen crew go to MedBay
6. **Repeat** — Threats get harder each mission

## ⚡ Specialization Bonuses
| Class | Bonus | Against |
|-------|-------|---------|
| 🔵 Pilot | +2 skill | Navigation threats (Asteroid Storm, Meteor Shower) |
| 🟡 Engineer | +2 skill | Technical threats (Hull Breach, Fuel Leak, Reactor Meltdown) |
| 🟢 Medic | +2 skill | Medical threats (Oxygen Failure) |
| 🟣 Scientist | +2 skill | Science threats (Solar Flare, Cosmic Radiation) |
| 🔴 Soldier | +2 skill | Combat threats (Alien Attack, Rogue AI) |

## ✅ Bonus Features Implemented
| Feature | Points |
|---------|--------|
| RecyclerView | +1 |
| Crew images per specialization | +1 |
| Mission visualization (HP bar + log) | +2 |
| Tactical combat (Attack / Defend / Special) | +2 |
| Statistics tracking | +1 |
| No death / MedBay recovery system | +1 |
| Randomness in missions | +1 |
| Specialization bonuses | +2 |
| Larger squads (up to 3 members) | +2 |
| Fragments UI | +2 |
| Data save and load | +2 |

## 🏗️ OOP Design
- **Encapsulation** — all fields private, accessed via getters/setters
- **Inheritance** — `CrewMember` base class extended by `Pilot`, `Engineer`, `Medic`, `Scientist`, `Soldier`
- **Polymorphism** — `MissionAction` interface implemented by `AttackAction`, `DefendAction`, `SpecialAction`. Each crew subclass overrides `act(Threat)` for specialization bonuses.

## 📦 Package Structure
```
com.example.spacecolony
├── model        — CrewMember, 5 subclasses, Threat
├── control      — Quarters, Simulator, MissionControl
├── mission      — MissionAction interface, 3 action classes
├── storage      — Storage
├── data         — GameData
├── statistics   — StatisticsManager, CrewStats
├── medbay       — MedBay
├── filemanager  — FileManager
└── ui.fragments — MainActivity, 8 fragments
```

## 📊 UML Diagram
See `UML_diagram_final.pdf` in this repository.

## 🎬 Video Demonstration
[Video Link — add here]

## 🛠️ Tools Used
- Android Studio
- Java
- Lucidchart (UML diagram)
- GitHub
- Claude, ChatGPT (debugging and documentation assistance)
