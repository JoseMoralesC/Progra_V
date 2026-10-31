import React, { useEffect, useRef } from 'react';
export function Modal({ titulo, onClose, children, actions }) {
  const dialog = useRef(null), previous = useRef(document.activeElement);
  useEffect(() => {
    document.body.classList.add('modal-open'); dialog.current?.focus();
    const key = event => {
      if (event.key === 'Escape') onClose();
      if (event.key === 'Tab') {
        const nodes = [...dialog.current.querySelectorAll('button,input,select,[tabindex]:not([tabindex="-1"])')].filter(x => !x.disabled);
        if (!nodes.length) return;
        const [first, last] = [nodes[0], nodes[nodes.length - 1]];
        if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last.focus(); }
        else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus(); }
      }
    };
    document.addEventListener('keydown', key);
    return () => { document.body.classList.remove('modal-open'); document.removeEventListener('keydown', key); previous.current?.focus?.(); };
  }, [onClose]);
  return <div className="backdrop open" role="presentation"><section ref={dialog} className="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title" tabIndex="-1">
    <header className="modal-head"><h2 id="modal-title">{titulo}</h2><button className="close" aria-label="Cerrar" onClick={onClose}>×</button></header>
    <div className="modal-body">{children}</div><footer className="modal-actions">{actions}</footer>
  </section></div>;
}
