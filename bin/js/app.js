/* ============================================================
   Shared shell: sidebar navigation + topbar.
   Each page calls renderShell({ active, title, subhead }) once
   the DOM is ready, then renders its own page content.
   ============================================================ */

const NAV_ITEMS = [
  { href: "index.html", icon: "bi-speedometer2", label: "Dashboard" },
  { href: "role-profiles.html", icon: "bi-person-badge", label: "Role Profiles" },
  { href: "diagnostic-test.html", icon: "bi-pencil-square", label: "Diagnostic Test" },
  { href: "skill-gap.html", icon: "bi-graph-up-arrow", label: "Skill-Gap Dashboard" },
  { href: "learning-plan.html", icon: "bi-map", label: "Learning Plan" },
  { href: "mock-interview.html", icon: "bi-chat-dots", label: "Mock Interview Log" },
  { href: "analytics.html", icon: "bi-activity", label: "Progress Analytics" }
];

function initials(name) {
  return name.split(" ").map(p => p[0]).join("").slice(0, 2).toUpperCase();
}

function renderShell({ active, title, subhead }) {
  const state = loadState();

  const sidebar = document.getElementById("sidebar");
  sidebar.innerHTML = `
    <div class="sidebar-inner" id="sidebarInner">
      <div class="brand">
        <div class="brand-mark">PR</div>
        <div class="brand-text">Placement Readiness Coach
          <small>Skill-Gap Roadmaps</small>
        </div>
      </div>
      <ul class="nav-list">
        ${NAV_ITEMS.map(item => `
          <li>
            <a class="nav-link ${item.href === active ? "active" : ""}" href="${item.href}">
              <i class="bi ${item.icon}"></i> ${item.label}
            </a>
          </li>`).join("")}
      </ul>
      <div class="sidebar-foot">
        Target role<br>
        <strong style="color:#E9EEF3">${getRoleById(state.student.targetRoleId)?.name || "Not set"}</strong>
      </div>
    </div>
  `;

  const topbar = document.getElementById("topbar");
  topbar.innerHTML = `
    <div>
      <h1>${title}</h1>
      ${subhead ? `<div class="subhead">${subhead}</div>` : ""}
    </div>
    <div class="d-flex align-items-center gap-2">
      <button class="btn btn-sm btn-outline-ink d-md-none" id="menuToggle"><i class="bi bi-list"></i></button>
      <div class="user-chip">
        <div class="avatar">${initials(state.student.name)}</div>
        <div>
          <div style="font-weight:600; line-height:1.1">${state.student.name}</div>
          <div style="color:var(--slate); font-size:0.72rem;">${state.student.roll}</div>
        </div>
      </div>
    </div>
  `;

  const toggle = document.getElementById("menuToggle");
  if (toggle) {
    toggle.addEventListener("click", () => {
      document.getElementById("sidebarInner").classList.toggle("open");
    });
  }

  return state;
}

function skillLevelClass(pct) {
  if (pct < 55) return "low";
  if (pct < 75) return "mid";
  return "high";
}

function pillForGap(gap) {
  if (gap <= 0) return `<span class="pill pill-success"><i class="bi bi-check-circle"></i> On target</span>`;
  if (gap <= 15) return `<span class="pill pill-warning"><i class="bi bi-dash-circle"></i> ${gap} pt gap</span>`;
  return `<span class="pill pill-danger"><i class="bi bi-exclamation-circle"></i> ${gap} pt gap</span>`;
}

function formatDate(iso) {
  return new Date(iso).toLocaleDateString("en-IN", { day: "numeric", month: "short", year: "numeric" });
}
