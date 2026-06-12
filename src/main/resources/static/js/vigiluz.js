

const API = {
  async post(url, data) {
    const res = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return res.json();
  },
  async get(url) {
    const res = await fetch(url);
    if (!res.ok) throw new Error('Not found');
    return res.json();
  },
  async patch(url, data) {
    const res = await fetch(url, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return res.json();
  }
};

function getBadgeClass(status) {
  const map = { ABERTO: 'badge-blue', TRIAGEM: 'badge-yellow', EM_EXECUCAO: 'badge-yellow', RESOLVIDO: 'badge-green', ENCERRADO: 'badge-gray' };
  return map[status] || 'badge-gray';
}
function getStatusLabel(status) {
  const map = { ABERTO: 'Aberto', TRIAGEM: 'Em Análise', EM_EXECUCAO: 'Em Execução', RESOLVIDO: 'Resolvido', ENCERRADO: 'Encerrado' };
  return map[status] || status;
}
function getPrioClass(p) {
  const map = { ALTA: 'badge-red', MEDIA: 'badge-yellow', BAIXA: 'badge-green' };
  return map[p] || 'badge-gray';
}
function getCategoriaLabel(c) {
  const map = { POSTE_APAGADO: 'Poste Apagado', LUZ_PISCANDO: 'Luz Piscando', FIACAO_EXPOSTA: 'Fiação Exposta' };
  return map[c] || c;
}
function formatDate(str) {
  if (!str) return '';
  const d = new Date(str);
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', {hour:'2-digit',minute:'2-digit'});
}

async function submitDenuncia(e) {
  e.preventDefault();
  const form = e.target;
  const btn = form.querySelector('[type=submit]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner"></span> Enviando...';

  const data = {
    descricao: form.descricao.value,
    bairro: form.bairro.value,
    rua: form.rua ? form.rua.value : '',
    categoria: form.categoria.value,
    prioridade: form.prioridade.value,
    anonima: form.anonima.checked
  };

  try {
    const res = await API.post('/solicitacoes', data);
    sessionStorage.setItem('ultimoProtocolo', JSON.stringify(res));
    window.location.href = '/protocolo';
  } catch (err) {
    btn.disabled = false;
    btn.textContent = 'Enviar';
    alert('Erro ao enviar. Tente novamente.');
  }
}

function loadProtocolo() {
  const div = document.getElementById('protocolo-result');
  if (!div) return;
  const data = JSON.parse(sessionStorage.getItem('ultimoProtocolo') || '{}');
  if (data.protocolo) {
    div.querySelector('.protocol-number').textContent = data.protocolo;
    div.querySelector('.protocol-status').textContent = 'Status: Aberto';
    div.style.display = 'block';
    document.getElementById('btn-consultar').href = '/consultar?p=' + data.protocolo;
  }
}

async function consultarSolicitacao(e) {
  e.preventDefault();
  const protocolo = document.getElementById('protocolo-input').value.trim();
  if (!protocolo) return;
  sessionStorage.setItem('consultarProtocolo', protocolo);
  window.location.href = '/acompanhamento?p=' + encodeURIComponent(protocolo);
}

function preloadConsultaInput() {
  const params = new URLSearchParams(location.search);
  const p = params.get('p');
  const input = document.getElementById('protocolo-input');
  if (p && input) input.value = p;
}

async function loadAcompanhamento() {
  const params = new URLSearchParams(location.search);
  const protocolo = params.get('p') || sessionStorage.getItem('consultarProtocolo');
  if (!protocolo) { window.location.href = '/consultar'; return; }

  const container = document.getElementById('acomp-container');
  const loading = document.getElementById('acomp-loading');
  const error = document.getElementById('acomp-error');

  try {
    const data = await API.get('/solicitacoes/' + encodeURIComponent(protocolo));
    loading.style.display = 'none';
    container.style.display = 'block';

    document.getElementById('acomp-protocolo').textContent = data.protocolo;
    document.getElementById('acomp-tipo').textContent = getCategoriaLabel(data.categoria);
    document.getElementById('acomp-bairro').textContent = data.bairro;
    document.getElementById('acomp-status').innerHTML = `<span class="badge ${getBadgeClass(data.statusAtual)}">${getStatusLabel(data.statusAtual)}</span>`;
    document.getElementById('acomp-prioridade').innerHTML = `<span class="badge ${getPrioClass(data.prioridade)}">${data.prioridade}</span>`;

    const allStatus = ['ABERTO','TRIAGEM','EM_EXECUCAO','RESOLVIDO'];
    const currentIdx = allStatus.indexOf(data.statusAtual);
    const histMap = {};
    (data.historico || []).forEach(h => { histMap[h.status] = h; });

    const tl = document.getElementById('timeline');
    tl.innerHTML = allStatus.map((s, i) => {
      const h = histMap[s] || {};
      const cls = i < currentIdx ? 'done' : (i === currentIdx ? 'active' : '');
      return `<li>
        <div class="timeline-dot ${cls}">${cls==='done' ? '✓' : ''}</div>
        <div class="timeline-content">
          <div class="timeline-title">${getStatusLabel(s)}</div>
          ${h.dataHora ? `<div class="timeline-date">${formatDate(h.dataHora)}</div>` : ''}
          ${h.comentario ? `<div class="timeline-desc">${h.comentario}</div>` : ''}
        </div>
      </li>`;
    }).join('');
  } catch (err) {
    loading.style.display = 'none';
    error.style.display = 'block';
    error.querySelector('.error-msg').textContent = 'Protocolo não encontrado: ' + protocolo;
  }
}

const MOCK_SOLICITACOES = [
  { protocolo:'VIGI-20260610-0001', categoria:'POSTE_APAGADO', bairro:'Centro', prioridade:'ALTA', statusAtual:'ABERTO', dataAbertura:'2026-06-10T08:00:00' },
  { protocolo:'VIGI-20260610-0002', categoria:'LUZ_PISCANDO', bairro:'Jardim América', prioridade:'MEDIA', statusAtual:'TRIAGEM', dataAbertura:'2026-06-09T14:30:00' },
  { protocolo:'VIGI-20260609-0001', categoria:'FIACAO_EXPOSTA', bairro:'Vila Nova', prioridade:'BAIXA', statusAtual:'RESOLVIDO', dataAbertura:'2026-06-09T11:00:00' },
  { protocolo:'VIGI-20260608-0003', categoria:'POSTE_APAGADO', bairro:'Jd. Europa', prioridade:'ALTA', statusAtual:'EM_EXECUCAO', dataAbertura:'2026-06-08T09:15:00' },
  { protocolo:'VIGI-20260608-0004', categoria:'LUZ_PISCANDO', bairro:'São Pedro', prioridade:'MEDIA', statusAtual:'ABERTO', dataAbertura:'2026-06-08T16:45:00' },
  { protocolo:'VIGI-20260607-0001', categoria:'POSTE_APAGADO', bairro:'Centro', prioridade:'BAIXA', statusAtual:'RESOLVIDO', dataAbertura:'2026-06-07T10:00:00' },
];

async function loadPainel() {
  const tbody = document.getElementById('painel-tbody');
  if (!tbody) return;

  let solicitacoes = [];
  try {
    solicitacoes = await API.get('/solicitacoes');
    if (!solicitacoes || solicitacoes.length === 0) throw new Error('empty');
  } catch {
    solicitacoes = MOCK_SOLICITACOES;
  }

  window._solicitacoes = solicitacoes;
  renderPainelTable(solicitacoes);
  updateStats(solicitacoes);
  renderChart(solicitacoes);
}

function renderPainelTable(lista) {
  const tbody = document.getElementById('painel-tbody');
  if (!lista.length) {
    tbody.innerHTML = '<tr><td colspan="7" class="empty-state">Nenhuma solicitação encontrada.</td></tr>';
    return;
  }
  tbody.innerHTML = lista.map(s => `
    <tr>
      <td><code style="font-size:0.78rem;color:var(--blue-mid)">${s.protocolo}</code></td>
      <td>${getCategoriaLabel(s.categoria)}</td>
      <td>${s.bairro}</td>
      <td><span class="badge ${getPrioClass(s.prioridade)}">${s.prioridade}</span></td>
      <td><span class="badge ${getBadgeClass(s.statusAtual)}">${getStatusLabel(s.statusAtual)}</span></td>
      <td>${s.dataAbertura ? formatDate(s.dataAbertura) : '-'}</td>
      <td><button class="btn btn-primary btn-sm" onclick="irParaDetalhes('${s.protocolo}')">Visualizar</button></td>
    </tr>
  `).join('');
}

function updateStats(lista) {
  const total = lista.length;
  const abertas = lista.filter(s => s.statusAtual === 'ABERTO').length;
  const analise = lista.filter(s => ['TRIAGEM','EM_EXECUCAO'].includes(s.statusAtual)).length;
  const resolvidas = lista.filter(s => ['RESOLVIDO','ENCERRADO'].includes(s.statusAtual)).length;
  document.getElementById('stat-total').textContent = total;
  document.getElementById('stat-abertas').textContent = abertas;
  document.getElementById('stat-analise').textContent = analise;
  document.getElementById('stat-resolvidas').textContent = resolvidas;
}

function filtrarPainel() {
  const status = document.getElementById('filtro-status').value;
  const bairro = document.getElementById('filtro-bairro').value.toLowerCase();
  const prio = document.getElementById('filtro-prio').value;
  let lista = window._solicitacoes || [];
  if (status) lista = lista.filter(s => s.statusAtual === status);
  if (bairro) lista = lista.filter(s => s.bairro.toLowerCase().includes(bairro));
  if (prio) lista = lista.filter(s => s.prioridade === prio);
  renderPainelTable(lista);
}

function irParaDetalhes(protocolo) {
  sessionStorage.setItem('detalheProtocolo', protocolo);
  window.location.href = '/funcionario/detalhes?p=' + encodeURIComponent(protocolo);
}

function renderChart(lista) {
  const canvas = document.getElementById('chart-status');
  if (!canvas || !window.Chart) return;
  const aberto = lista.filter(s => s.statusAtual === 'ABERTO').length;
  const triagem = lista.filter(s => s.statusAtual === 'TRIAGEM').length;
  const execucao = lista.filter(s => s.statusAtual === 'EM_EXECUCAO').length;
  const resolvido = lista.filter(s => ['RESOLVIDO','ENCERRADO'].includes(s.statusAtual)).length;
  new Chart(canvas, {
    type: 'doughnut',
    data: {
      labels: ['Aberto', 'Em Análise', 'Em Execução', 'Resolvido'],
      datasets: [{ data: [aberto, triagem, execucao, resolvido], backgroundColor: ['#2563EB','#D97706','#7C3AED','#16A34A'], borderWidth: 0 }]
    },
    options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
  });
  const canvasBar = document.getElementById('chart-bairro');
  if (!canvasBar) return;
  const bairros = {};
  lista.forEach(s => { bairros[s.bairro] = (bairros[s.bairro] || 0) + 1; });
  new Chart(canvasBar, {
    type: 'bar',
    data: {
      labels: Object.keys(bairros),
      datasets: [{ label: 'Solicitações', data: Object.values(bairros), backgroundColor: '#2563EB', borderRadius: 6 }]
    },
    options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
  });
}

async function loadDetalhes() {
  const params = new URLSearchParams(location.search);
  const protocolo = params.get('p') || sessionStorage.getItem('detalheProtocolo');
  if (!protocolo) { window.location.href = '/funcionario/painel'; return; }

  let data;
  try {
    data = await API.get('/solicitacoes/' + encodeURIComponent(protocolo));
  } catch {
    data = MOCK_SOLICITACOES.find(s => s.protocolo === protocolo) || MOCK_SOLICITACOES[0];
    data.historico = [
      { status: 'ABERTO', dataHora: '2026-06-10T08:00:00', comentario: 'Solicitação registrada pelo cidadão.' },
      { status: 'TRIAGEM', dataHora: '2026-06-10T10:30:00', comentario: 'Equipe técnica avaliando a situação.' }
    ];
    data.descricao = data.descricao || 'Poste apagado na esquina da rua principal.';
  }

  document.getElementById('det-protocolo').textContent = data.protocolo;
  document.getElementById('det-tipo').textContent = getCategoriaLabel(data.categoria);
  document.getElementById('det-bairro').textContent = data.bairro;
  document.getElementById('det-status').innerHTML = `<span class="badge ${getBadgeClass(data.statusAtual)}">${getStatusLabel(data.statusAtual)}</span>`;
  document.getElementById('det-prio').innerHTML = `<span class="badge ${getPrioClass(data.prioridade)}">${data.prioridade}</span>`;
  document.getElementById('det-descricao').textContent = data.descricao || '-';

  const tl = document.getElementById('det-timeline');
  tl.innerHTML = (data.historico || []).map(h => `
    <li>
      <div class="timeline-dot done">✓</div>
      <div class="timeline-content">
        <div class="timeline-title">${getStatusLabel(h.status)}</div>
        <div class="timeline-date">${formatDate(h.dataHora)}</div>
        ${h.comentario ? `<div class="timeline-desc">${h.comentario}</div>` : ''}
      </div>
    </li>
  `).join('');

  window._currentProtocolo = protocolo;
}

async function salvarStatus(e) {
  e.preventDefault();
  const btn = e.target.querySelector('[type=submit]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner"></span> Salvando...';
  const novoStatus = document.getElementById('novo-status').value;
  const comentario = document.getElementById('comentario').value;
  const protocolo = window._currentProtocolo;

  try {
    await API.patch('/solicitacoes/' + encodeURIComponent(protocolo) + '/status', { novoStatus, comentario });
    showToast('Status atualizado com sucesso!', 'success');
    setTimeout(() => loadDetalhes(), 600);
  } catch {
    showToast('Erro ao atualizar status.', 'error');
  }
  btn.disabled = false;
  btn.textContent = 'Salvar Alteração';
}

function loginFuncionario(e) {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const senha = document.getElementById('senha').value;
  if (email && senha) {
    sessionStorage.setItem('funcionario', JSON.stringify({ email, nome: 'Funcionário' }));
    window.location.href = '/funcionario/painel';
  } else {
    showToast('Preencha e-mail e senha.', 'error');
  }
}

function showToast(msg, type='success') {
  let t = document.getElementById('toast');
  if (!t) {
    t = document.createElement('div');
    t.id = 'toast';
    t.style.cssText = 'position:fixed;bottom:1.5rem;right:1.5rem;padding:0.75rem 1.25rem;border-radius:10px;font-size:0.875rem;font-weight:600;z-index:9999;transition:opacity 0.3s;box-shadow:0 4px 12px rgba(0,0,0,0.15)';
    document.body.appendChild(t);
  }
  t.textContent = msg;
  t.style.background = type === 'success' ? '#16A34A' : '#DC2626';
  t.style.color = 'white';
  t.style.opacity = '1';
  clearTimeout(t._timeout);
  t._timeout = setTimeout(() => { t.style.opacity = '0'; }, 3000);
}

document.addEventListener('DOMContentLoaded', () => {
  const path = location.pathname;
  if (path === '/protocolo') loadProtocolo();
  if (path === '/consultar') preloadConsultaInput();
  if (path === '/acompanhamento') loadAcompanhamento();
  if (path === '/funcionario/painel') loadPainel();
  if (path === '/funcionario/detalhes') loadDetalhes();
});
