const userId = 'default-user';
const apiHeaders = {
  'Content-Type': 'application/json',
  'X-User-Id': userId
};

const promptInput = document.getElementById('promptInput');
const sendButton = document.getElementById('sendButton');
const chatMessages = document.getElementById('chatMessages');
const planBadge = document.getElementById('planBadge');
const quotaText = document.getElementById('quotaText');
const quotaBar = document.getElementById('quotaBar');
const rateText = document.getElementById('rateText');
const rateCountdown = document.getElementById('rateCountdown');
const tokenEstimate = document.getElementById('tokenEstimate');
const historyChart = document.getElementById('historyChart');
const upgradeModal = document.getElementById('upgradeModal');
const upgradeButton = document.getElementById('upgradeButton');
const closeModal = document.getElementById('closeModal');

let quotaData = { plan: 'FREE', monthlyQuota: 1000, monthlyUsedTokens: 0, rateLimitRemaining: 5, rateLimitResetSeconds: 60, dailyUsage: [0,0,0,0,0,0,0] };
let upgradePlan = 'FREE';

promptInput.addEventListener('input', () => {
  tokenEstimate.textContent = estimateTokens(promptInput.value);
});

sendButton.addEventListener('click', sendPrompt);
closeModal.addEventListener('click', () => upgradeModal.classList.add('hidden'));
upgradeButton.addEventListener('click', simulateUpgrade);

window.addEventListener('load', () => {
  refreshUsage();
  setInterval(refreshUsage, 5000);
  setInterval(updateCountdown, 1000);
});

async function refreshUsage() {
  try {
    const response = await fetch('/api/usage', { headers: apiHeaders });
    const data = await response.json();
    quotaData = data;
    updateUiFromQuota(data);
  } catch (error) {
    console.error('Error al consultar uso:', error);
  }
}

function updateUiFromQuota(data) {
  const remaining = Math.max(0, data.monthlyQuota - data.monthlyUsedTokens);
  const used = data.monthlyUsedTokens;
  const percent = Math.min(100, (used / data.monthlyQuota) * 100);
  quotaText.textContent = `${used} / ${data.monthlyQuota}`;
  quotaBar.style.width = `${percent}%`;
  rateText.textContent = `Rate limit: ${data.rateLimitRemaining} requests/min`;
  rateCountdown.textContent = `Reset en ${data.rateLimitResetSeconds}s`;
  planBadge.textContent = data.plan;
  planBadge.className = `badge ${data.plan.toLowerCase()}`;
  const planLabel = document.getElementById('planLabel');
  if (planLabel) {
    planLabel.textContent = data.plan;
  }
  renderHistory(data.dailyUsage || []);
  sendButton.disabled = data.rateLimitRemaining <= 0 || remaining <= 0;
}

function renderHistory(values) {
  historyChart.innerHTML = '';
  const max = Math.max(...values, 1);
  values.slice(-7).forEach((value) => {
    const bar = document.createElement('div');
    bar.className = 'bar';
    bar.style.height = `${(value / max) * 100}%`;
    bar.innerHTML = `<span>${value}</span>`;
    historyChart.appendChild(bar);
  });
}

function estimateTokens(prompt) {
  if (!prompt || !prompt.trim()) return 0;
  return Math.max(1, Math.min(200, Math.floor(prompt.trim().length / 4)));
}

async function sendPrompt() {
  const prompt = promptInput.value.trim();
  if (!prompt) return;
  appendMessage('user', prompt);
  sendButton.disabled = true;
  promptInput.value = '';
  tokenEstimate.textContent = '0';

  appendLoadingMessage();

  try {
    const response = await fetch('/api/chat', {
      method: 'POST',
      headers: apiHeaders,
      body: JSON.stringify({ prompt })
    });
    const data = await response.json();
    removeLoadingMessage();

    if (!response.ok) {
      if (response.status === 429 || data.blocked) {
        showUpgradeModal();
      }
      appendMessage('bot', data.message || 'Error en la petición.');
    } else {
      appendMessage('bot', data.message);
      refreshUsage();
    }
  } catch (error) {
    removeLoadingMessage();
    appendMessage('bot', 'Error de conexión con el backend.');
    console.error(error);
  } finally {
    sendButton.disabled = false;
  }
}

function appendMessage(type, text) {
  const wrapper = document.createElement('div');
  wrapper.className = `message ${type}`;
  wrapper.innerHTML = `
    <div class="bubble">${escapeHtml(text)}</div>
    <div class="meta">${type === 'user' ? 'Tú' : 'Proxy IA'} · ${new Date().toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })}</div>
  `;
  chatMessages.appendChild(wrapper);
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

function appendLoadingMessage() {
  removeLoadingMessage();
  const wrapper = document.createElement('div');
  wrapper.id = 'loadingMessage';
  wrapper.className = 'message bot';
  wrapper.innerHTML = `
    <div class="bubble">Escribiendo respuesta...</div>
    <div class="meta">Proxy IA · ${new Date().toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })}</div>
  `;
  chatMessages.appendChild(wrapper);
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

function removeLoadingMessage() {
  const loading = document.getElementById('loadingMessage');
  if (loading) loading.remove();
}

function escapeHtml(value) {
  return value.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function updateCountdown() {
  if (!quotaData) return;
  if (quotaData.rateLimitResetSeconds > 0) {
    quotaData.rateLimitResetSeconds -= 1;
    rateCountdown.textContent = `Reset en ${quotaData.rateLimitResetSeconds}s`;
  }
}

function showUpgradeModal() {
  upgradeModal.classList.remove('hidden');
}

function simulateUpgrade() {
  upgradeModal.classList.add('hidden');
  if (upgradePlan === 'FREE') {
    upgradePlan = 'PRO';
    planBadge.textContent = 'PRO';
    planBadge.className = 'badge pro';
    quotaData.plan = 'PRO';
    quotaData.monthlyQuota = 5000;
    quotaData.rateLimitRemaining = 20;
    quotaData.monthlyUsedTokens = 0;
    quotaText.textContent = `${quotaData.monthlyUsedTokens} / ${quotaData.monthlyQuota}`;
    quotaBar.style.width = '0%';
    rateText.textContent = 'Rate limit: 20 requests/min';
    sendButton.disabled = false;
  }
}
