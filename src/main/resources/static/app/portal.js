const state = { token: localStorage.getItem('printflow.token'), user: null, units: [], unit: null, printers: [], filter: 'TODOS' };
const $ = selector => document.querySelector(selector);
const esc = value => String(value ?? '').replace(/[&<>'"]/g, char => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', "'":'&#39;', '"':'&quot;' })[char]);
const API_BASE_URL = 'http://localhost:8080/api';

function toast(message, error = false) {
  const el = $('#toast'); el.textContent = message;
  el.className = `fixed right-5 top-5 z-50 rounded-xl px-4 py-3 text-sm font-medium shadow-lg ${error ? 'bg-rose-600 text-white' : 'bg-slate-900 text-white'}`;
  setTimeout(() => el.classList.add('hidden'), 3500);
}
function logout() { localStorage.removeItem('printflow.token'); state.token = null; state.user = null; $('#appScreen').classList.add('hidden'); $('#loginScreen').classList.remove('hidden'); }
async function api(path, options = {}) {
  const isFormData = options.body instanceof FormData;
  const response = await fetch(`${API_BASE_URL}${path}`, { cache: 'no-store', ...options, headers: { ...(isFormData ? {} : { 'Content-Type':'application/json' }), ...(state.token ? { Authorization: `Bearer ${state.token}` } : {}), ...(options.headers || {}) } });
  if (response.status === 401) { logout(); throw new Error('Sua sessão expirou.'); }
  const text = await response.text(); let data; try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!response.ok) throw new Error(data?.message || data || 'Não foi possível concluir esta ação.');
  return data;
}
function statusName(status) { return ({ ATIVA:'Ativa', BACKUP:'Backup', ESTOQUE:'Estoque', MANUTENCAO:'Manutenção', DESATIVADA:'Desativada' })[status] || status; }
function statusClass(status) { return ({ ATIVA:'bg-emerald-50 text-emerald-700', BACKUP:'bg-blue-50 text-blue-700', ESTOQUE:'bg-amber-50 text-amber-700', MANUTENCAO:'bg-violet-50 text-violet-700', DESATIVADA:'bg-slate-100 text-slate-600' })[status] || 'bg-slate-100 text-slate-600'; }
function input(label, id, type = 'text', value = '', required = true) { return `<label class="block text-sm font-medium">${label}<input id="${id}" ${required ? 'required' : ''} type="${type}" value="${esc(value)}" class="mt-1.5 w-full rounded-xl border border-slate-300 px-3 py-2.5 outline-none focus:border-brand-500 focus:ring-4 focus:ring-blue-100"></label>`; }
function select(label, id, values) { return `<label class="block text-sm font-medium">${label}<select id="${id}" required class="mt-1.5 w-full rounded-xl border border-slate-300 px-3 py-2.5 outline-none focus:border-brand-500 focus:ring-4 focus:ring-blue-100">${values.map(value => `<option value="${value}">${statusName(value)}</option>`).join('')}</select></label>`; }

function showModal({ eyebrow, title, body, submit }) {
  $('#modalEyebrow').textContent = eyebrow; $('#modalTitle').textContent = title; $('#modalBody').innerHTML = body; $('#modalError').textContent = '';
  $('#modalForm').onsubmit = async event => { event.preventDefault(); try { await submit(); $('#modal').close(); } catch (error) { $('#modalError').textContent = error.message; } };
  $('#modal').showModal();
}
document.querySelectorAll('[data-close]').forEach(button => button.onclick = () => $('#modal').close());

async function loadUnits() { state.units = await api('/projects'); renderUnits(); }
function renderUnits() {
  $('#projectsGrid').innerHTML = state.units.length ? state.units.map(unit => `<button class="rounded-2xl border border-slate-200 bg-white p-5 text-left shadow-sm transition hover:-translate-y-0.5 hover:border-blue-200 hover:shadow-md" data-unit="${unit.id}"><div class="grid h-11 w-11 place-items-center rounded-xl bg-brand-50 font-bold text-brand-600">${esc(unit.name).slice(0, 1)}</div><h2 class="mt-4 text-lg font-bold">${esc(unit.name)}</h2><p class="mt-1 line-clamp-2 text-sm text-slate-500">${esc(unit.description || 'Unidade de operação')}</p><p class="mt-4 text-sm font-semibold text-brand-600">${unit.printerCount || 0} impressora(s) →</p></button>`).join('') : '<div class="col-span-full rounded-2xl border border-dashed border-slate-300 bg-white p-10 text-center text-slate-500">Nenhuma unidade cadastrada. Crie a primeira unidade para começar.</div>';
  document.querySelectorAll('[data-unit]').forEach(button => button.onclick = () => openUnit(button.dataset.unit));
}
async function openUnit(id) { state.unit = state.units.find(unit => String(unit.id) === String(id)); state.filter = 'TODOS'; $('#projectsScreen').classList.add('hidden'); $('#inventoryScreen').classList.remove('hidden'); $('#unitTitle').textContent = state.unit.name; await loadInventory(); }
async function loadInventory() { const [printers, history] = await Promise.all([api(`/projects/${state.unit.id}/impressora`), api(`/projects/${state.unit.id}/movimentacoesImpressora`)]); state.printers = printers; renderInventory(history); }
function renderInventory(history) {
  const counts = ['ATIVA','BACKUP','ESTOQUE'].map(status => ({ status, total: state.printers.filter(printer => printer.status === status).length }));
  $('#metrics').innerHTML = counts.map(item => `<div class="rounded-2xl border border-slate-200 bg-white p-5"><p class="text-sm text-slate-500">${statusName(item.status)}</p><p class="mt-1 text-3xl font-bold">${item.total}</p></div>`).join('');
  const filters = ['TODOS','ATIVA','BACKUP','ESTOQUE','MANUTENCAO','DESATIVADA'];
  $('#filters').innerHTML = filters.map(filter => `<button data-filter="${filter}" class="rounded-lg px-3 py-2 text-xs font-semibold ${state.filter === filter ? 'bg-brand-600 text-white' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'}">${filter === 'TODOS' ? 'Todas' : statusName(filter)}</button>`).join('');
  document.querySelectorAll('[data-filter]').forEach(button => button.onclick = () => { state.filter = button.dataset.filter; renderInventory(history); });
  const visible = state.printers.filter(printer => state.filter === 'TODOS' || printer.status === state.filter);
  $('#printersTable').innerHTML = visible.length ? visible.map(printer => `<tr class="border-t border-slate-100"><td class="px-5 py-4 font-semibold">${esc(printer.assetTag || '—')}</td><td class="px-5 py-4"><p class="font-medium">${esc(printer.modelo)}</p><p class="text-xs text-slate-500">${esc(printer.serial || 'Sem serial')}</p></td><td class="px-5 py-4">${esc(printer.local)}</td><td class="px-5 py-4 text-slate-600">${esc(printer.ip || '—')}</td><td class="px-5 py-4"><span class="rounded-full px-2.5 py-1 text-xs font-bold ${statusClass(printer.status)}">${statusName(printer.status)}</span></td><td class="px-5 py-4 text-right"><button class="font-semibold text-brand-600 hover:underline" data-move="${printer.id}">Movimentar</button>${printer.status === 'ATIVA' ? `<button class="ml-3 font-semibold text-brand-600 hover:underline" data-swap="${printer.id}">Inverter</button>` : ''}</td></tr>`).join('') : '<tr><td colspan="6" class="px-5 py-10 text-center text-slate-500">Nenhuma impressora neste filtro.</td></tr>';
  document.querySelectorAll('[data-move]').forEach(button => button.onclick = () => movePrinter(button.dataset.move)); document.querySelectorAll('[data-swap]').forEach(button => button.onclick = () => swapPrinter(button.dataset.swap));
  $('#history').innerHTML = history.length ? history.slice(0, 8).map(item => `<div class="flex gap-3 border-b border-slate-100 pb-3"><span class="mt-1 h-2 w-2 rounded-full bg-brand-500"></span><div><p class="font-medium">${esc(item.descricaoMotivo || 'Movimentação registrada')}</p><p class="text-sm text-slate-500">${esc(item.serialImpressora)} · ${esc(item.localOrigem)} → ${esc(item.localDestino)}</p></div></div>`).join('') : '<p class="text-sm text-slate-500">Ainda não há movimentações nesta unidade.</p>';
}
function newUnit() { showModal({ eyebrow:'Configuração', title:'Nova unidade', body: input('Nome da unidade', 'unitName'), submit: async () => { const name = $('#unitName').value.trim(); await api('/projects/criarLocal', { method:'POST', body:JSON.stringify({ unidade:name, nomeLocal:name }) }); toast('Unidade criada.'); await loadUnits(); } }); }
function newPrinter() { showModal({ eyebrow:'Inventário', title:'Cadastrar impressora', body: input('Modelo', 'model') + input('Serial', 'serial') + input('Etiqueta patrimonial', 'assetTag') + input('IP', 'ip', 'text', '', false) + input('Local / setor', 'localName') + select('Status inicial', 'status', ['ATIVA','BACKUP','ESTOQUE']), submit: async () => { const local = await api('/projects/criarLocal', { method:'POST', body:JSON.stringify({ unidade:state.unit.name, nomeLocal:$('#localName').value.trim() }) }); await api('/projects', { method:'POST', body:JSON.stringify({ model:$('#model').value.trim(), serial:$('#serial').value.trim(), assetTag:$('#assetTag').value.trim(), ip:$('#ip').value.trim(), local:String(local.idLocal), status:$('#status').value }) }); toast('Impressora cadastrada.'); await loadInventory(); } }); }
async function movePrinter(id) {
  const printer = state.printers.find(item => String(item.id) === String(id));
  let options;
  try { options = await api(`/projects/status/${printer.status}/transicoes`); }
  catch (error) { return toast(error.message, true); }
  if (!options.length) return toast('Este status não possui transições permitidas.', true);

  const locations = [...new Set(state.printers.map(item => item.local).filter(local => local && local !== 'Sem local'))];
  const replacement = `<div id="replacementField" class="hidden"><label class="block text-sm font-medium">Impressora ativa que será substituída<select id="replacement" class="mt-1.5 w-full rounded-xl border border-slate-300 px-3 py-2.5"></select></label><p class="mt-1 text-xs text-slate-500">A impressora de backup assumirá automaticamente o local da impressora selecionada.</p></div>`;
  const destination = `<label id="destinationField" class="block text-sm font-medium">Local de destino<input id="destination" required list="knownLocations" value="${esc(printer.local)}" class="mt-1.5 w-full rounded-xl border border-slate-300 px-3 py-2.5 outline-none focus:border-brand-500 focus:ring-4 focus:ring-blue-100"><datalist id="knownLocations">${locations.map(local => `<option value="${esc(local)}">`).join('')}</datalist></label>`;
  showModal({ eyebrow:'Movimentação', title:`Mover ${printer.assetTag || printer.serial}`, body: destination + select('Novo status', 'status', options) + replacement + `<label class="block text-sm font-medium">Descrição<textarea id="description" required class="mt-1.5 min-h-24 w-full rounded-xl border border-slate-300 px-3 py-2.5 outline-none focus:border-brand-500 focus:ring-4 focus:ring-blue-100" placeholder="Descreva o motivo da movimentação"></textarea></label>`, submit: async () => {
    const status = $('#status').value;
    const description = $('#description').value.trim();
    const replacementId = $('#replacement').value;
    const requiresReplacement = printer.status === 'BACKUP' && status === 'ATIVA';
    if (requiresReplacement && !replacementId) throw new Error('Selecione a impressora ativa que será substituída.');
    if (requiresReplacement) {
      await api('/projects/inverter', { method:'POST', body:JSON.stringify({ sourcePrinterId:Number(replacementId), targetPrinterId:Number(printer.id), descricaoMotivo:description }) });
    } else {
      const localName = $('#destination').value.trim();
      if (!localName) throw new Error('Informe o local de destino.');
      const location = await api('/projects/criarLocal', { method:'POST', body:JSON.stringify({ unidade:state.unit.name, nomeLocal:localName }) });
      await api('/projects/movimentar', { method:'POST', body:JSON.stringify({ model:printer.modelo, serial:printer.serial, assetTag:printer.assetTag, ip:printer.ip, local:String(location.idLocal), status, descricaoMotivo:description }) });
    }
    toast('Movimentação registrada.'); await loadInventory();
  }});
  const refreshReplacement = () => {
    const useReplacement = printer.status === 'BACKUP' && $('#status').value === 'ATIVA';
    $('#replacementField').classList.toggle('hidden', !useReplacement);
    $('#destinationField').classList.toggle('hidden', useReplacement);
    $('#destination').required = !useReplacement;
    if (useReplacement) {
      const active = state.printers.filter(item => item.status === 'ATIVA');
      $('#replacement').innerHTML = active.map(item => `<option value="${item.id}">${esc(item.assetTag || item.serial)} · ${esc(item.local)}</option>`).join('');
      if (!active.length) $('#modalError').textContent = 'Não há impressora ativa disponível para substituição.';
    }
  };
  $('#status').onchange = refreshReplacement;
  refreshReplacement();
}
function swapPrinter(id) { const active = state.printers.find(item => String(item.id) === String(id)); const backups = state.printers.filter(item => item.status === 'BACKUP'); if (!backups.length) return toast('Cadastre ou mova uma impressora para Backup antes de inverter.', true); showModal({ eyebrow:'Inversão', title:'Trocar equipamento ativo', body:`<p class="text-sm text-slate-600">A impressora ativa irá para Backup e a selecionada assumirá o local atual.</p><label class="block text-sm font-medium">Impressora de backup<select id="backup" class="mt-1.5 w-full rounded-xl border border-slate-300 px-3 py-2.5">${backups.map(item => `<option value="${item.id}">${esc(item.assetTag || item.serial)} · ${esc(item.modelo)}</option>`).join('')}</select></label>`, submit:async () => { await api('/projects/inverter', { method:'POST', body:JSON.stringify({ sourcePrinterId:Number(active.id), targetPrinterId:Number($('#backup').value) }) }); toast('Inversão concluída.'); await loadInventory(); } }); }

$('#loginForm').onsubmit = async event => { event.preventDefault(); try { const data = await api('/auth/login', { method:'POST', body:JSON.stringify({ email:$('#email').value.trim(), password:$('#password').value }) }); state.token = data.token; state.user = data.user; localStorage.setItem('printflow.token', data.token); $('#userName').textContent = data.user.name; $('#avatar').textContent = data.user.name.split(' ').map(name => name[0]).join('').slice(0,2); $('#usersButton').classList.toggle('hidden', data.user.role !== 'admin'); $('#loginScreen').classList.add('hidden'); $('#appScreen').classList.remove('hidden'); await loadUnits(); } catch (error) { $('#loginError').textContent = error.message; } };
$('#showRegister').onclick = () => showModal({ eyebrow:'Acesso', title:'Solicitar acesso', body:input('Nome completo','registerName') + input('E-mail','registerEmail','email') + input('Senha','registerPassword','password'), submit:async () => { await api('/auth/register',{method:'POST',body:JSON.stringify({name:$('#registerName').value.trim(),email:$('#registerEmail').value.trim(),password:$('#registerPassword').value})}); toast('Solicitação enviada para aprovação.'); } });
$('#newUnitButton').onclick = newUnit; $('#newPrinterButton').onclick = newPrinter; $('#backButton').onclick = () => { $('#inventoryScreen').classList.add('hidden'); $('#projectsScreen').classList.remove('hidden'); }; $('#homeButton').onclick = () => { $('#inventoryScreen').classList.add('hidden'); $('#projectsScreen').classList.remove('hidden'); }; $('#logoutButton').onclick = logout;
$('#importButton').onclick = () => $('#importFile').click();
$('#importFile').onchange = async event => {
  const file = event.target.files[0];
  if (!file) return;
  try {
    const form = new FormData();
    form.append('file', file);
    const message = await api('/projects/upload', { method: 'POST', body: form });
    toast(message || 'Planilha importada com sucesso.');
    await loadInventory();
  } catch (error) {
    toast(error.message, true);
  } finally {
    event.target.value = '';
  }
};
$('#usersButton').onclick = async () => { try { const users = await api('/users'); showModal({ eyebrow:'Administração', title:'Aprovação de usuários', body: users.map(user => `<div class="flex items-center justify-between rounded-xl bg-slate-50 p-3"><div><p class="font-semibold">${esc(user.name)}</p><p class="text-xs text-slate-500">${esc(user.email)} · ${user.approved ? 'Aprovado' : 'Pendente'}</p></div>${!user.approved ? `<button type="button" data-approve="${user.id}" class="rounded-lg bg-brand-600 px-3 py-2 text-sm font-semibold text-white">Aprovar</button>` : ''}</div>`).join(''), submit:async () => {} }); document.querySelectorAll('[data-approve]').forEach(button => button.onclick = async () => { try { await api(`/users/${button.dataset.approve}/approve`,{method:'PATCH'}); button.closest('div.flex').remove(); toast('Usuário aprovado.'); } catch(error) { toast(error.message, true); } }); } catch(error) { toast(error.message,true); } };
