/* ============================================================
   Mock data layer.
   Everything is stored in localStorage under the "prc_" prefix
   so the prototype behaves statefully without a backend.
   Replace the functions in here with real fetch() calls to your
   Spring Boot API once it exists — the page code never touches
   localStorage directly, only these functions.
   ============================================================ */

const STORAGE_KEY = "prc_state_v1";

const ROLE_LIBRARY = [
  {
    id: "backend-java",
    name: "Backend Developer (Java)",
    icon: "bi-hdd-stack",
    summary: "Server-side APIs, data modelling and system design with Java/Spring.",
    skills: [
      { name: "Core Java & OOP", target: 85 },
      { name: "Spring Boot", target: 80 },
      { name: "SQL & Data Modelling", target: 80 },
      { name: "REST API Design", target: 75 },
      { name: "DSA", target: 80 },
      { name: "System Design Basics", target: 65 }
    ]
  },
  {
    id: "frontend-react",
    name: "Frontend Developer (React)",
    icon: "bi-layout-text-window",
    summary: "Component-driven UI, state management and web performance.",
    skills: [
      { name: "JavaScript (ES6+)", target: 85 },
      { name: "React", target: 80 },
      { name: "HTML/CSS", target: 80 },
      { name: "REST/API Integration", target: 70 },
      { name: "DSA", target: 65 },
      { name: "Testing (Jest/RTL)", target: 55 }
    ]
  },
  {
    id: "data-analyst",
    name: "Data Analyst",
    icon: "bi-bar-chart-line",
    summary: "SQL, statistics and dashboards that turn data into decisions.",
    skills: [
      { name: "SQL", target: 85 },
      { name: "Excel", target: 70 },
      { name: "Python (pandas)", target: 75 },
      { name: "Statistics", target: 70 },
      { name: "Data Visualisation", target: 70 },
      { name: "Business Communication", target: 60 }
    ]
  },
  {
    id: "qa-engineer",
    name: "QA / Test Engineer",
    icon: "bi-clipboard2-check",
    summary: "Manual and automated testing across the software lifecycle.",
    skills: [
      { name: "Manual Testing", target: 80 },
      { name: "Test Case Design", target: 75 },
      { name: "Selenium/Automation", target: 70 },
      { name: "SQL", target: 60 },
      { name: "API Testing", target: 65 },
      { name: "Bug Tracking Tools", target: 55 }
    ]
  }
];

const DIAGNOSTIC_QUESTIONS = [
  {
    id: "q1",
    topic: "Core Java & OOP",
    prompt: "What does the 'final' keyword do when applied to a Java variable?",
    options: [
      "It makes the variable's value unchangeable after assignment",
      "It makes the variable visible only within the current package",
      "It forces the variable to be stored on the heap",
      "It allows the variable to be overridden in a subclass"
    ],
    answer: 0
  },
  {
    id: "q2",
    topic: "DSA",
    prompt: "What is the average time complexity of searching in a balanced binary search tree?",
    options: ["O(1)", "O(log n)", "O(n)", "O(n log n)"],
    answer: 1
  },
  {
    id: "q3",
    topic: "SQL & Data Modelling",
    prompt: "Which SQL clause is used to filter groups after a GROUP BY?",
    options: ["WHERE", "HAVING", "FILTER", "ORDER BY"],
    answer: 1
  },
  {
    id: "q4",
    topic: "REST API Design",
    prompt: "Which HTTP status code correctly indicates a successful resource creation?",
    options: ["200 OK", "201 Created", "204 No Content", "302 Found"],
    answer: 1
  },
  {
    id: "q5",
    topic: "Spring Boot",
    prompt: "Which annotation marks a class as a REST controller in Spring Boot?",
    options: ["@Service", "@Entity", "@RestController", "@Repository"],
    answer: 2
  },
  {
    id: "q6",
    topic: "System Design Basics",
    prompt: "What is the main purpose of a load balancer in a web system?",
    options: [
      "To encrypt traffic between client and server",
      "To distribute incoming requests across multiple servers",
      "To store frequently accessed data closer to the user",
      "To validate a user's login credentials"
    ],
    answer: 1
  }
];

const LEARNING_RESOURCES = {
  "Core Java & OOP": ["Revise inheritance, polymorphism and interfaces", "Solve 15 OOP-design practice problems"],
  "Spring Boot": ["Build a small CRUD REST API with Spring Boot + JPA", "Learn dependency injection and Spring profiles"],
  "SQL & Data Modelling": ["Practice joins, subqueries and window functions", "Design a normalized schema for a sample app"],
  "REST API Design": ["Study REST conventions and status codes", "Document one API with OpenAPI/Swagger"],
  "DSA": ["Daily 2 problems: arrays, strings, trees", "Revise time/space complexity analysis"],
  "System Design Basics": ["Read on load balancing, caching and horizontal scaling", "Sketch the design of a URL shortener"],
  "JavaScript (ES6+)": ["Practice closures, promises and async/await", "Build a small utility library with tests"],
  "React": ["Build a component with hooks and context", "Learn controlled forms and lifting state up"],
  "HTML/CSS": ["Rebuild a responsive layout with Flexbox/Grid", "Audit a page for accessibility issues"]
};

function defaultState() {
  return {
    student: { name: "Aditi Rao", roll: "TYIT-42", targetRoleId: "backend-java" },
    diagnostics: {},         // { roleId: { score, byTopic: {topic: pct}, takenAt } }
    learningPlan: {},        // { roleId: [{ id, topic, task, done }] }
    interviews: [],          // [{ id, role, date, rating, notes }]
    analyticsHistory: []     // [{ date, readiness }]
  };
}

function loadState() {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    const seeded = seedState(defaultState());
    localStorage.setItem(STORAGE_KEY, JSON.stringify(seeded));
    return seeded;
  }
  try {
    return JSON.parse(raw);
  } catch (e) {
    const seeded = seedState(defaultState());
    localStorage.setItem(STORAGE_KEY, JSON.stringify(seeded));
    return seeded;
  }
}

function saveState(state) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

/* Seed one realistic diagnostic result + a partly-done learning plan +
   two interview logs + a short analytics history, so every page has
   something meaningful to show on first load. */
function seedState(state) {
  const roleId = "backend-java";
  state.diagnostics[roleId] = {
    score: 63,
    byTopic: {
      "Core Java & OOP": 78,
      "Spring Boot": 52,
      "SQL & Data Modelling": 70,
      "REST API Design": 60,
      "DSA": 55,
      "System Design Basics": 40
    },
    takenAt: "2026-08-20"
  };

  state.learningPlan[roleId] = [
    { id: "lp1", topic: "System Design Basics", task: "Read on load balancing, caching and horizontal scaling", done: false },
    { id: "lp2", topic: "Spring Boot", task: "Build a small CRUD REST API with Spring Boot + JPA", done: true },
    { id: "lp3", topic: "DSA", task: "Daily 2 problems: arrays, strings, trees", done: false },
    { id: "lp4", topic: "REST API Design", task: "Document one API with OpenAPI/Swagger", done: false }
  ];

  state.interviews = [
    { id: "iv1", role: "Backend Developer (Java)", date: "2026-08-12", rating: 3, notes: "Confident on Java basics; struggled to explain indexing trade-offs in SQL." },
    { id: "iv2", role: "Backend Developer (Java)", date: "2026-08-25", rating: 4, notes: "Clear on REST design; needs a sharper answer for 'how would you scale this service'." }
  ];

  state.analyticsHistory = [
    { date: "2026-07-01", readiness: 41 },
    { date: "2026-07-15", readiness: 47 },
    { date: "2026-08-01", readiness: 55 },
    { date: "2026-08-20", readiness: 63 }
  ];

  return state;
}

function getRoleById(id) {
  return ROLE_LIBRARY.find(r => r.id === id);
}
